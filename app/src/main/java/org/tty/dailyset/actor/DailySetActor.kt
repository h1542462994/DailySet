package org.tty.dailyset.actor

import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tty.dailyset.bean.ResponseCodes
import org.tty.dailyset.bean.entity.*
import org.tty.dailyset.bean.enums.*
import org.tty.dailyset.bean.lifetime.DailySetRC
import org.tty.dailyset.bean.lifetime.DailySetSummary
import org.tty.dailyset.bean.lifetime.DailySetTRC
import org.tty.dailyset.bean.req.DailySetUpdateReq
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.common.util.Diff
import org.tty.dailyset.component.common.BaseVM
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.datasource.DataSourceCollection
import org.tty.dioc.util.optional
import java.time.LocalDateTime

/**
 * actor for [DailySetTable],[DailySetRow],[DailySetCell], and .. entities start with **DailySet**.
 * interaction between [BaseVM] and [DataSourceCollection]
 * @see ActorCollection
 */
class DailySetActor(private val sharedComponents: SharedComponents) {

    private var job: Job? = null
    private var sharedFlow = MutableSharedFlow<Int>(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val dailySetTypedUpdateAdapter = DailySetTypedUpdateAdapter(sharedComponents)

    fun startUpdateData() {
        if (job == null) {
            job = sharedComponents.applicationScope.launch {
                withContext(Dispatchers.IO) {
                    sharedFlow.collect {
                        doUpdateData()
                    }
                }
            }
        }
    }

    fun endUpdateData() {
        if (job != null) {
            job?.cancel()
            job = null
        }
    }

    fun updateData() {
        sharedFlow.tryEmit(1)
    }

    private suspend fun doUpdateData() {
        try {
            val userUid: String =
                sharedComponents.actorCollection.preferenceActor.read(PreferenceName.CURRENT_USER_UID)


            val responseDailySetInfos =
                sharedComponents.dataSourceCollection.netSourceCollection.dailySetService.dailySetInfo()
            if (responseDailySetInfos.code == ResponseCodes.success || responseDailySetInfos.code == ResponseCodes.ticketNotExist) {
                if (responseDailySetInfos.data != null) {
                    withDailySetInfos(responseDailySetInfos.data, userUid)
                }
            }
        } catch (e: Exception) {
            logger.e("DailySetActor", "update data failed, ${e.message}")
            e.printStackTrace()
        }

    }

    private suspend fun withDailySetInfos(dailySets: List<DailySet>, userUid: String) {
        sharedComponents.database.withTransaction {
            val dailySetVisibles =
                sharedComponents.database.dailySetVisibleDao().allByUserUid(userUid)
            val diff = Diff<DailySetVisible, DailySet, String> {
                source = dailySetVisibles
                target = dailySets
                sourceKeySelector = { it.dailySetUid }
                targetKeySelector = { it.uid }
            }

            // to removes, set the visible to false.
            if (diff.removes.isNotEmpty()) {
                val removes = diff.removes.map { it.copy(visible = false) }
                sharedComponents.database.dailySetVisibleDao().updateBatch(removes)
            }
            // to sames, set the visible to true. and update the dailyset.
            if (diff.sames.isNotEmpty()) {
                val updates = diff.sames.map {
                    it.source.copy(visible = true)
                }
                sharedComponents.database.dailySetVisibleDao().updateBatch(updates)
            }
            // to adds, create the dailySetVisibles and insert
            if (diff.adds.isNotEmpty()) {
                val adds = diff.adds.map {
                    DailySetVisible(
                        userUid = userUid,
                        dailySetUid = it.uid,
                        visible = true
                    )
                }
                sharedComponents.database.dailySetVisibleDao().updateBatch(adds)
            }

            dailySets.forEach {
                withDailySet(it)
            }

            // update the dailyset
            //sharedComponents.database.dailySetDao().updateBatch(dailySets)
        }
    }

    private suspend fun withDailySet(dailySet: DailySet) {
        var oldDailySet = sharedComponents.database.dailySetDao().get(dailySet.uid)
        if (oldDailySet == null) {
            // mark all version to 0 to ensure receive all data.
            oldDailySet = DailySet(dailySet.uid, dailySet.type, 0, 0, 0)
        }

        val updateRawResult =
            sharedComponents.dataSourceCollection.netSourceCollection.dailySetService.dailySetUpdate(
                DailySetUpdateReq(
                    uid = oldDailySet.uid,
                    type = oldDailySet.type,
                    sourceVersion = oldDailySet.sourceVersion,
                    matteVersion = oldDailySet.matteVersion,
                    metaVersion = oldDailySet.metaVersion
                )
            )

        if (updateRawResult.code == ResponseCodes.success) {
            if (updateRawResult.data != null) {
                updateRawResult.data.updateItems.forEach {
                    //withUpdateItem(dailySet, it)
                    dailySetTypedUpdateAdapter.withUpdateItem(dailySet, it)
                }

                // update the dailyset.
                sharedComponents.database.dailySetDao().update(updateRawResult.data.dailySet)
            }
        }
    }

    suspend fun getDailySetSummaries(): List<DailySetSummary> {
        val shownDailySetTypes = listOf(
            DailySetType.Normal.value,
            DailySetType.Clazz.value,
            DailySetType.ClazzAuto.value,
            DailySetType.Task.value
        )

        val userUid: String =
            sharedComponents.actorCollection.preferenceActor.read(PreferenceName.CURRENT_USER_UID)
        val dailySets = sharedComponents.database.dailySetDao().all()
        val dailySetVisibleUid =
            sharedComponents.database.dailySetVisibleDao().allByUserUid(userUid)
                .filter { it.visible }
                .map { it.dailySetUid }
        val visibleDailySets = dailySets.filter { dailySet -> dailySet.uid in dailySetVisibleUid }
        val shownDailySets = visibleDailySets.filter { it.type in shownDailySetTypes }

        val results = shownDailySets.map {
            getDailySetSummary(it)
        }

        return results
    }

    private suspend fun getDailySetSummary(dailySet: DailySet): DailySetSummary {
        val matchedUid = dailySet.uid.toMatchedUid()
        var dailySetBasicMeta: DailySetBasicMeta? = null
        var link: DailySetMetaLinks? = null
        // query the raw source.
        link = sharedComponents.database.dailySetMetaLinksDao()
            .anyBySetUidAndMetaTypeNoLocal(matchedUid, DailySetMetaType.BasicMeta.value)
        if (link != null) {
            dailySetBasicMeta =
                sharedComponents.database.dailySetBasicMetaDao().anyByMetaUid(link.metaUid)

            val localDailySetBasicMeta =
                sharedComponents.database.dailySetBasicMetaDao().anyByMetaUid(link.metaUid.toLocalStarUid())
            if (localDailySetBasicMeta != null) {
                dailySetBasicMeta = localDailySetBasicMeta
            }
        }


        // query ok
        return DailySetSummary(
            uid = dailySet.uid,
            type = DailySetType.of(dailySet.type),
            name = dailySetBasicMeta.optional { name } ?: "<未命名>",
            icon = DailySetIcon.of(dailySetBasicMeta.optional { icon } ?: "")
        )
    }

    suspend fun getDailySetSummary(dailySetUid: String): DailySetSummary? {
        val dailySet = sharedComponents.database.dailySetDao().get(dailySetUid) ?: return null
        return getDailySetSummary(dailySet)
    }

    suspend fun getDailySetDurations(dailySetUid: String): List<DailySetDuration> {
        val dailySet =
            sharedComponents.database.dailySetDao().get(dailySetUid) ?: return emptyList()
        val uid = dailySet.uid
        val autoUidRegex = "^#school.zjut.course.[\\dA-Za-z_-]+$".toRegex()
        val referTo = "#school.zjut.unic"

        val durations = mutableListOf<DailySetDuration>()
        if (autoUidRegex.matches(uid)) {
            val dailySetDurationUids = sharedComponents.database.dailySetSourceLinksDao()
                .allBySetUidAndSourceType(referTo, DailySetSourceType.Duration.value)
                .filter { it.removeVersion <= 0 }
                .map { it.sourceUid }
            if (dailySetDurationUids.isNotEmpty()) {
                durations.addAll(
                    sharedComponents.database.dailySetDurationDao()
                        .allDurationsBySourceUids(dailySetDurationUids)
                )
            }

        }

        return durations
    }

    suspend fun getDailySetTRC(dailySetUid: String): DailySetTRC? {
        val dailySet = sharedComponents.database.dailySetDao().get(dailySetUid) ?: return null
        val uid = dailySet.uid
        val autoUidRegex = "^#school.zjut.course.[\\dA-Za-z_-]+$".toRegex()
        val referTo = "#school.zjut.global"

        val dailySetTables = mutableListOf<DailySetTable>()
        val dailySetRows = mutableListOf<DailySetRow>()
        val dailySetCells = mutableListOf<DailySetCell>()

        if (autoUidRegex.matches(uid)) {
            val dailySetTableUids = sharedComponents.database.dailySetSourceLinksDao()
                .allBySetUidAndSourceType(referTo, DailySetSourceType.Table.value)
                .filter { it.removeVersion <= 0 }
                .map { it.sourceUid }
            if (dailySetTableUids.isNotEmpty()) {
                dailySetTables.addAll(
                    sharedComponents.database.dailySetTableDao().allBySourceUids(dailySetTableUids)
                )
            }
            val dailySetRowUids = sharedComponents.database.dailySetSourceLinksDao()
                .allBySetUidAndSourceType(referTo, DailySetSourceType.Row.value)
                .filter { it.removeVersion <= 0 }
                .map { it.sourceUid }
            if (dailySetRowUids.isNotEmpty()) {
                dailySetRows.addAll(
                    sharedComponents.database.dailySetRowDao().allBySourceUids(dailySetRowUids)
                )
            }
            val dailySetCellUids = sharedComponents.database.dailySetSourceLinksDao()
                .allBySetUidAndSourceType(referTo, DailySetSourceType.Cell.value)
                .filter { it.removeVersion <= 0 }
                .map { it.sourceUid }
            if (dailySetCellUids.isNotEmpty()) {
                dailySetCells.addAll(
                    sharedComponents.database.dailySetCellDao().allBySourceUids(dailySetCellUids)
                )
            }
        }

        return join3Source(dailySetTables, dailySetRows, dailySetCells)
    }

    private fun join3Source(
        dailySetTables: List<DailySetTable>,
        dailySetRows: List<DailySetRow>,
        dailySetCells: List<DailySetCell>
    ): DailySetTRC? {
        if (dailySetTables.isEmpty()) return null
        val dailySetTable = dailySetTables[0]
        return DailySetTRC(
            dailySetTable = dailySetTable,
            dailySetRCs = dailySetRows.filter { it.tableUid == dailySetTable.sourceUid }
                .map { row ->
                    DailySetRC(
                        dailySetRow = row,
                        dailySetCells = dailySetCells.filter { it.rowUid == row.sourceUid }
                    )
                }
        )
    }

    /**
     * 获取某个时间段的课程表信息
     */
    suspend fun getDailySetCourses(
        dailySetUid: String,
        year: Int,
        periodCode: DailySetPeriodCode
    ): List<DailySetCourse> {
        val dailySet =
            sharedComponents.database.dailySetDao().get(dailySetUid) ?: return emptyList()
        val dailySetCourseUids =
            sharedComponents.database.dailySetSourceLinksDao()
                .allBySetUidAndSourceType(dailySet.uid, DailySetSourceType.Course.value)
                .filter { it.removeVersion <= 0 }
                .map { it.sourceUid }
        var dailySetCourses = emptyList<DailySetCourse>()
        if (dailySetCourseUids.isNotEmpty()) {
            dailySetCourses = sharedComponents.database.dailySetCourseDao().findAllBySourceUidsAndYearPeriodCode(
                dailySetCourseUids,
                year,
                periodCode = periodCode.code
            )
        }

        return dailySetCourses
    }

    suspend fun renameDailySet(dailySetSummary: DailySetSummary) {
        sharedComponents.database.withTransaction {
            val matchedUid = dailySetSummary.uid.toMatchedUid()
            sharedComponents.database.dailySetDao().get(matchedUid) ?: return@withTransaction
            val dailySetMetaLinks =
                sharedComponents.database.dailySetMetaLinksDao().anyBySetUidAndMetaTypeNoLocal(matchedUid, DailySetMetaType.BasicMeta.value) ?: return@withTransaction
            val dailySetBasicMeta =
                sharedComponents.database.dailySetBasicMetaDao().anyByMetaUid(dailySetMetaLinks.metaUid) ?: return@withTransaction

            val localDailySetMetaLinks =
                dailySetMetaLinks.copy(
                    metaUid = dailySetBasicMeta.metaUid.toLocalStarUid(),
                    dailySetUid = matchedUid,
                    metaType = DailySetMetaType.BasicMeta.value,
                    insertVersion = EntityDefaults.LOCAL_VERSION_DISABLE,
                    updateVersion = EntityDefaults.LOCAL_VERSION_ENABLE,
                    removeVersion = EntityDefaults.LOCAL_VERSION_DISABLE,
                    lastTick = LocalDateTime.now()
                )
            val localDailySetBasicMeta =
                dailySetBasicMeta.copy(
                    metaUid = dailySetBasicMeta.metaUid.toLocalStarUid(),
                    name = dailySetSummary.name,
                    icon = dailySetSummary.icon.toStoreValue()
                )
            sharedComponents.database.dailySetMetaLinksDao().updateBatch(listOf(localDailySetMetaLinks))
            sharedComponents.database.dailySetBasicMetaDao().update(localDailySetBasicMeta)
        }

    }


    companion object {
        private val CLAZZ_AUTO_REGEX = "^#school.[\\dA-Za-z_-]+.course.[\\dA-Za-z_-]+$".toRegex()
        private const val LOCAL_SUFFIX = ".local"

        /**
         * if the raw uid like [CLAZZ_AUTO_REGEX], then apply **.g** after it
         */
        private fun String.toMatchedUid(): String {
            return if (CLAZZ_AUTO_REGEX.matches(this)) {
                "$this.g"
            } else {
                this
            }
        }

        private fun String.isClazzAuto(): Boolean {
            return CLAZZ_AUTO_REGEX.matches(this)
        }

        /**
         * apply **.local** after it
         */
        private fun String.toLocalStarUid(): String {
            return "$this${LOCAL_SUFFIX}"
        }



    }

}