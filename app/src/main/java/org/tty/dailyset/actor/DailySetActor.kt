package org.tty.dailyset.actor

import androidx.room.withTransaction
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import org.tty.dailyset.bean.DailySetUpdateItem
import org.tty.dailyset.bean.DailySetUpdateItemCollection
import org.tty.dailyset.bean.ResponseCodes
import org.tty.dailyset.bean.entity.*
import org.tty.dailyset.bean.enums.*
import org.tty.dailyset.bean.lifetime.DailySetRC
import org.tty.dailyset.bean.lifetime.DailySetSummary
import org.tty.dailyset.bean.lifetime.DailySetTRC
import org.tty.dailyset.bean.req.DailySetUpdateReq
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.common.util.Diff
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dioc.util.optional
import java.util.*

/**
 * repository for [DailySetTable],[DailySetRow],[DailySetCell]
 * it is used in [org.tty.dailyset.DailySetApplication],
 * it will use db service, see also [org.tty.dailyset.database.DailySetRoomDatabase]
 */
class DailySetActor(private val sharedComponents: SharedComponents) {

    suspend fun updateData() {
        doUpdateData()
    }

    private suspend fun doUpdateData() {
        try {
            val userUid: String =
                sharedComponents.actorCollection.preferenceActor.read(PreferenceName.CURRENT_USER_UID)


            val responseDailySetInfos =
                sharedComponents.dataSourceCollection.netSourceCollection.dailySetService.dailySetInfo()
            if (responseDailySetInfos.code == ResponseCodes.success) {
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
                    withUpdateItem(dailySet, it)
                }

                // update the dailyset.
                sharedComponents.database.dailySetDao().update(updateRawResult.data.dailySet)
            }
        }
    }

    private suspend fun withUpdateItem(
        dailySet: DailySet,
        rawUpdateItemCollection: DailySetUpdateItemCollection<JsonElement>
    ) {
        when (rawUpdateItemCollection.type) {
            DailySetDataType.Source.value -> {
                when (rawUpdateItemCollection.subType) {
                    DailySetSourceType.Table.value -> withDailySetTableUpdateItem(
                        dailySet,
                        castTo(rawUpdateItemCollection)
                    )
                    DailySetSourceType.Row.value -> withDailySetRowUpdateItem(
                        dailySet,
                        castTo(rawUpdateItemCollection)
                    )
                    DailySetSourceType.Cell.value -> withDailySetCellUpdateItem(
                        dailySet,
                        castTo(rawUpdateItemCollection)
                    )
                    DailySetSourceType.Duration.value -> withDailySetDurationUpdateItem(
                        dailySet,
                        castTo(rawUpdateItemCollection)
                    )
                    DailySetSourceType.Course.value -> withDailySetCourseUpdateItem(
                        dailySet,
                        castTo(rawUpdateItemCollection)
                    )
                }
            }
            DailySetDataType.Meta.value -> {
                when (rawUpdateItemCollection.subType) {
                    DailySetMetaType.BasicMeta.value -> withDailySetBasicMetaUpdateItem(
                        dailySet,
                        castTo(rawUpdateItemCollection)
                    )
                    DailySetMetaType.UsageMeta.value -> withDailySetUsageMetaUpdateItem(
                        dailySet,
                        castTo(rawUpdateItemCollection)
                    )
                    DailySetMetaType.SchoolMeta.value -> withDailySetSchoolInfoMetaUpdateItem(
                        dailySet,
                        castTo(rawUpdateItemCollection)
                    )
                    DailySetMetaType.StudentInfoMeta.value -> withDailySetStudentInfoMetaUpdateItem(
                        dailySet,
                        castTo(rawUpdateItemCollection)
                    )
                }
            }
        }
    }

    //region update all resources
    private suspend fun withDailySetTableUpdateItem(
        dailySet: DailySet,
        updateItemCollection: DailySetUpdateItemCollection<DailySetTable>
    ) {
        val targetLinks = updateItemCollection.updates.map {
            DailySetSourceLinks(
                dailySetUid = dailySet.uid,
                sourceType = DailySetSourceType.Table.value,
                sourceUid = it.data.sourceUid,
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick
            )
        }
        val sourceLinks =
            sharedComponents.database.dailySetSourceLinksDao().allBySetUidAndSourceType(
                dailySetUid = dailySet.uid,
                sourceType = DailySetSourceType.Table.value
            )
        // get diffs
        val diff = Diff<DailySetSourceLinks, DailySetSourceLinks, String> {
            source = sourceLinks
            target = targetLinks
            sourceKeySelector = { it.sourceUid }
            targetKeySelector = { it.sourceUid }
        }
        // with removes, remove
        //sharedComponents.database.dailySetSourceLinksDao().removeBatch(diff.removes)
        // with same, update with targets
        sharedComponents.database.dailySetSourceLinksDao().updateBatch(diff.sames.map { it.target })
        // with adds, update
        sharedComponents.database.dailySetSourceLinksDao().updateBatch(diff.adds)

        // update the dailySetTables
        sharedComponents.database.dailySetTableDao()
            .updateBatch(updateItemCollection.updates.map { it.data })
    }

    private suspend fun withDailySetRowUpdateItem(
        dailySet: DailySet,
        updateItemCollection: DailySetUpdateItemCollection<DailySetRow>
    ) {
        val targetLinks = updateItemCollection.updates.map {
            DailySetSourceLinks(
                dailySetUid = dailySet.uid,
                sourceType = DailySetSourceType.Row.value,
                sourceUid = it.data.sourceUid,
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick
            )
        }
        val sourceLinks =
            sharedComponents.database.dailySetSourceLinksDao().allBySetUidAndSourceType(
                dailySetUid = dailySet.uid,
                sourceType = DailySetSourceType.Row.value
            )
        // get diffs
        val diff = Diff<DailySetSourceLinks, DailySetSourceLinks, String> {
            source = sourceLinks
            target = targetLinks
            sourceKeySelector = { it.sourceUid }
            targetKeySelector = { it.sourceUid }
        }
        // with removes, remove
        //sharedComponents.database.dailySetSourceLinksDao().removeBatch(diff.removes)
        // with same, update with targets
        sharedComponents.database.dailySetSourceLinksDao().updateBatch(diff.sames.map { it.target })
        // with adds, update
        sharedComponents.database.dailySetSourceLinksDao().updateBatch(diff.adds)

        // update the dailySetTables
        sharedComponents.database.dailySetRowDao()
            .updateBatch(updateItemCollection.updates.map { it.data })
    }

    private suspend fun withDailySetCellUpdateItem(
        dailySet: DailySet,
        updateItemCollection: DailySetUpdateItemCollection<DailySetCell>
    ) {
        val targetLinks = updateItemCollection.updates.map {
            DailySetSourceLinks(
                dailySetUid = dailySet.uid,
                sourceType = DailySetSourceType.Cell.value,
                sourceUid = it.data.sourceUid,
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick
            )
        }
        val sourceLinks =
            sharedComponents.database.dailySetSourceLinksDao().allBySetUidAndSourceType(
                dailySetUid = dailySet.uid,
                sourceType = DailySetSourceType.Cell.value
            )
        // get diffs
        val diff = Diff<DailySetSourceLinks, DailySetSourceLinks, String> {
            source = sourceLinks
            target = targetLinks
            sourceKeySelector = { it.sourceUid }
            targetKeySelector = { it.sourceUid }
        }
        // with removes, remove
        //sharedComponents.database.dailySetSourceLinksDao().removeBatch(diff.removes)
        // with same, update with targets
        sharedComponents.database.dailySetSourceLinksDao().updateBatch(diff.sames.map { it.target })
        // with adds, update
        sharedComponents.database.dailySetSourceLinksDao().updateBatch(diff.adds)

        // update the dailySetTables
        sharedComponents.database.dailySetCellDao()
            .updateBatch(updateItemCollection.updates.map { it.data })
    }

    private suspend fun withDailySetDurationUpdateItem(
        dailySet: DailySet,
        updateItemCollection: DailySetUpdateItemCollection<DailySetDuration>
    ) {
        val targetLinks = updateItemCollection.updates.map {
            DailySetSourceLinks(
                dailySetUid = dailySet.uid,
                sourceType = DailySetSourceType.Duration.value,
                sourceUid = it.data.sourceUid,
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick
            )
        }
        val sourceLinks =
            sharedComponents.database.dailySetSourceLinksDao().allBySetUidAndSourceType(
                dailySetUid = dailySet.uid,
                sourceType = DailySetSourceType.Duration.value
            )
        // get diffs
        val diff = Diff<DailySetSourceLinks, DailySetSourceLinks, String> {
            source = sourceLinks
            target = targetLinks
            sourceKeySelector = { it.sourceUid }
            targetKeySelector = { it.sourceUid }
        }
        // with removes, remove
        //sharedComponents.database.dailySetSourceLinksDao().removeBatch(diff.removes)
        // with same, update with targets
        sharedComponents.database.dailySetSourceLinksDao().updateBatch(diff.sames.map { it.target })
        // with adds, update
        sharedComponents.database.dailySetSourceLinksDao().updateBatch(diff.adds)

        // update the dailySetTables
        sharedComponents.database.dailySetDurationDao()
            .updateBatch(updateItemCollection.updates.map { it.data })
    }

    private suspend fun withDailySetCourseUpdateItem(
        dailySet: DailySet,
        updateItemCollection: DailySetUpdateItemCollection<DailySetCourse>
    ) {
        val targetLinks = updateItemCollection.updates.map {
            DailySetSourceLinks(
                dailySetUid = dailySet.uid,
                sourceType = DailySetSourceType.Course.value,
                sourceUid = it.data.sourceUid,
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick
            )
        }
        val sourceLinks =
            sharedComponents.database.dailySetSourceLinksDao().allBySetUidAndSourceType(
                dailySetUid = dailySet.uid,
                sourceType = DailySetSourceType.Course.value
            )
        // get diffs
        val diff = Diff<DailySetSourceLinks, DailySetSourceLinks, String> {
            source = sourceLinks
            target = targetLinks
            sourceKeySelector = { it.sourceUid }
            targetKeySelector = { it.sourceUid }
        }
        // with removes, remove
        //sharedComponents.database.dailySetSourceLinksDao().removeBatch(diff.removes)
        // with same, update with targets
        sharedComponents.database.dailySetSourceLinksDao().updateBatch(diff.sames.map { it.target })
        // with adds, update
        sharedComponents.database.dailySetSourceLinksDao().updateBatch(diff.adds)

        // update the dailySetTables
        sharedComponents.database.dailySetCourseDao()
            .updateBatch(updateItemCollection.updates.map { it.data })
    }

    private suspend fun withDailySetBasicMetaUpdateItem(
        dailySet: DailySet,
        updateItemCollection: DailySetUpdateItemCollection<DailySetBasicMeta>
    ) {
        val targetLinks = updateItemCollection.updates.map {
            DailySetMetaLinks(
                dailySetUid = dailySet.uid,
                metaType = DailySetMetaType.BasicMeta.value,
                metaUid = it.data.metaUid,
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick
            )
        }
        val sourceLinks =
            sharedComponents.database.dailySetMetaLinksDao().allBySetUidAndMetaType(
                dailySetUid = dailySet.uid,
                metaType = DailySetMetaType.BasicMeta.value
            )
        // get diffs
        val diff = Diff<DailySetMetaLinks, DailySetMetaLinks, String> {
            source = sourceLinks
            target = targetLinks
            sourceKeySelector = { it.metaUid }
            targetKeySelector = { it.metaUid }
        }
        // with removes, remove
        //sharedComponents.database.dailySetMetaLinksDao().removeBatch(diff.removes)
        // with same, update with targets
        sharedComponents.database.dailySetMetaLinksDao().updateBatch(diff.sames.map { it.target })
        // with adds, update
        sharedComponents.database.dailySetMetaLinksDao().updateBatch(diff.adds)

        // update the dailySetTables
        sharedComponents.database.dailySetBasicMetaDao()
            .updateBatch(updateItemCollection.updates.map { it.data })
    }

    private suspend fun withDailySetUsageMetaUpdateItem(
        dailySet: DailySet,
        updateItemCollection: DailySetUpdateItemCollection<DailySetUsageMeta>
    ) {
        val targetLinks = updateItemCollection.updates.map {
            DailySetMetaLinks(
                dailySetUid = dailySet.uid,
                metaType = DailySetMetaType.UsageMeta.value,
                metaUid = it.data.metaUid,
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick
            )
        }
        val sourceLinks =
            sharedComponents.database.dailySetMetaLinksDao().allBySetUidAndMetaType(
                dailySetUid = dailySet.uid,
                metaType = DailySetMetaType.UsageMeta.value
            )
        // get diffs
        val diff = Diff<DailySetMetaLinks, DailySetMetaLinks, String> {
            source = sourceLinks
            target = targetLinks
            sourceKeySelector = { it.metaUid }
            targetKeySelector = { it.metaUid }
        }
        // with removes, remove
        //sharedComponents.database.dailySetMetaLinksDao().removeBatch(diff.removes)
        // with same, update with targets
        sharedComponents.database.dailySetMetaLinksDao().updateBatch(diff.sames.map { it.target })
        // with adds, update
        sharedComponents.database.dailySetMetaLinksDao().updateBatch(diff.adds)

        // update the dailySetTables
        sharedComponents.database.dailySetUsageMetaDao()
            .updateBatch(updateItemCollection.updates.map { it.data })
    }

    private suspend fun withDailySetSchoolInfoMetaUpdateItem(
        dailySet: DailySet,
        updateItemCollection: DailySetUpdateItemCollection<DailySetSchoolInfoMeta>
    ) {
        val targetLinks = updateItemCollection.updates.map {
            DailySetMetaLinks(
                dailySetUid = dailySet.uid,
                metaType = DailySetMetaType.SchoolMeta.value,
                metaUid = it.data.metaUid,
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick
            )
        }
        val sourceLinks =
            sharedComponents.database.dailySetMetaLinksDao().allBySetUidAndMetaType(
                dailySetUid = dailySet.uid,
                metaType = DailySetMetaType.SchoolMeta.value
            )
        // get diffs
        val diff = Diff<DailySetMetaLinks, DailySetMetaLinks, String> {
            source = sourceLinks
            target = targetLinks
            sourceKeySelector = { it.metaUid }
            targetKeySelector = { it.metaUid }
        }
        // with removes, remove
        //sharedComponents.database.dailySetMetaLinksDao().removeBatch(diff.removes)
        // with same, update with targets
        sharedComponents.database.dailySetMetaLinksDao().updateBatch(diff.sames.map { it.target })
        // with adds, update
        sharedComponents.database.dailySetMetaLinksDao().updateBatch(diff.adds)

        // update the dailySetTables
        sharedComponents.database.dailySetSchoolInfoMetaDao()
            .updateBatch(updateItemCollection.updates.map { it.data })
    }

    private suspend fun withDailySetStudentInfoMetaUpdateItem(
        dailySet: DailySet,
        updateItemCollection: DailySetUpdateItemCollection<DailySetStudentInfoMeta>
    ) {
        val targetLinks = updateItemCollection.updates.map {
            DailySetMetaLinks(
                dailySetUid = dailySet.uid,
                metaType = DailySetMetaType.StudentInfoMeta.value,
                metaUid = it.data.metaUid,
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick
            )
        }
        val sourceLinks =
            sharedComponents.database.dailySetMetaLinksDao().allBySetUidAndMetaType(
                dailySetUid = dailySet.uid,
                metaType = DailySetMetaType.StudentInfoMeta.value
            )
        // get diffs
        val diff = Diff<DailySetMetaLinks, DailySetMetaLinks, String> {
            source = sourceLinks
            target = targetLinks
            sourceKeySelector = { it.metaUid }
            targetKeySelector = { it.metaUid }
        }
        // with removes, remove
        //sharedComponents.database.dailySetMetaLinksDao().removeBatch(diff.removes)
        // with same, update with targets
        sharedComponents.database.dailySetMetaLinksDao().updateBatch(diff.sames.map { it.target })
        // with adds, update
        sharedComponents.database.dailySetMetaLinksDao().updateBatch(diff.adds)

        // update the dailySetTables
        sharedComponents.database.dailySetStudentInfoMetaDao()
            .updateBatch(updateItemCollection.updates.map { it.data })
    }
    //endregion

    private inline fun <reified T : Any> castTo(rawUpdateItemCollection: DailySetUpdateItemCollection<JsonElement>): DailySetUpdateItemCollection<T> {
        return DailySetUpdateItemCollection(
            type = rawUpdateItemCollection.type,
            subType = rawUpdateItemCollection.subType,
            updates = rawUpdateItemCollection.updates.map {
                DailySetUpdateItem(
                    insertVersion = it.insertVersion,
                    updateVersion = it.updateVersion,
                    removeVersion = it.removeVersion,
                    lastTick = it.lastTick,
                    data = Json.decodeFromJsonElement(it.data)
                )
            }
        )
    }

    suspend fun getDailySetSummaries(): List<DailySetSummary> {
        val shownDailySetTypes = listOf(
            DailySetType.Normal.value,
            DailySetType.Clazz.value,
            DailySetType.ClazzAuto.value,
            DailySetType.Task.value
        )

        val userUid: String = sharedComponents.actorCollection.preferenceActor.read(PreferenceName.CURRENT_USER_UID)
        val dailySets = sharedComponents.database.dailySetDao().all()
        val dailySetVisibleUid = sharedComponents.database.dailySetVisibleDao().allByUserUid(userUid)
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
        val uid = dailySet.uid
        val uidG = dailySet.uid + ".g"
        var dailySetBasicMeta: DailySetBasicMeta? = null
        var link: DailySetMetaLinks? = null
        // query the basic_meta from uid
        link = sharedComponents.database.dailySetMetaLinksDao().anyBySetUidAndMetaType(uid, DailySetMetaType.BasicMeta.value)
        if (link != null) {
            dailySetBasicMeta = sharedComponents.database.dailySetBasicMetaDao().anyByMetaUid(link.metaUid)
        }
        // query the basic_meta
        if (dailySetBasicMeta == null) {
            link = sharedComponents.database.dailySetMetaLinksDao().anyBySetUidAndMetaType(uidG, DailySetMetaType.BasicMeta.value)
        }
        if (link != null) {
            dailySetBasicMeta = sharedComponents.database.dailySetBasicMetaDao().anyByMetaUid(link.metaUid)
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
        val dailySet = sharedComponents.database.dailySetDao().get(dailySetUid) ?: return emptyList()
        val uid = dailySet.uid
        val autoUidRegex = "^#school.zjut.course.[\\dA-Za-z_-]+$".toRegex()
        val referTo = "#school.zjut.unic"

        val durations = mutableListOf<DailySetDuration>()
        if (autoUidRegex.matches(uid)) {
            val dailySetDurationUids = sharedComponents.database.dailySetSourceLinksDao().allBySetUidAndSourceType(referTo, DailySetSourceType.Duration.value)
                .filter { it.removeVersion <= 0 }
                .map { it.sourceUid }
            if (dailySetDurationUids.isNotEmpty()) {
                durations.addAll(
                    sharedComponents.database.dailySetDurationDao().allDurationsBySourceUids(dailySetDurationUids)
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
            val dailySetTableUids = sharedComponents.database.dailySetSourceLinksDao().allBySetUidAndSourceType(referTo, DailySetSourceType.Table.value)
                .filter { it.removeVersion <= 0 }
                .map { it.sourceUid }
            if (dailySetTableUids.isNotEmpty()) {
                dailySetTables.addAll(sharedComponents.database.dailySetTableDao().allBySourceUids(dailySetTableUids))
            }
            val dailySetRowUids = sharedComponents.database.dailySetSourceLinksDao().allBySetUidAndSourceType(referTo, DailySetSourceType.Row.value)
                .filter { it.removeVersion <= 0 }
                .map { it.sourceUid }
            if (dailySetRowUids.isNotEmpty()) {
                dailySetRows.addAll(sharedComponents.database.dailySetRowDao().allBySourceUids(dailySetRowUids))
            }
            val dailySetCellUids = sharedComponents.database.dailySetSourceLinksDao().allBySetUidAndSourceType(referTo, DailySetSourceType.Cell.value)
                .filter { it.removeVersion <= 0 }
                .map { it.sourceUid }
            if (dailySetCellUids.isNotEmpty()) {
                dailySetCells.addAll(sharedComponents.database.dailySetCellDao().allBySourceUids(dailySetCellUids))
            }
        }

        return join3Source(dailySetTables, dailySetRows, dailySetCells)
    }

    private fun join3Source(dailySetTables: List<DailySetTable>, dailySetRows: List<DailySetRow>, dailySetCells: List<DailySetCell>): DailySetTRC? {
        if (dailySetTables.isEmpty()) return null
        val dailySetTable = dailySetTables[0]
        return DailySetTRC(
            dailySetTable = dailySetTable,
            dailySetRCs = dailySetRows.filter { it.tableUid == dailySetTable.sourceUid }.map { row ->
                DailySetRC(
                    dailySetRow = row,
                    dailySetCells = dailySetCells.filter { it.rowUid == row.sourceUid }
                )
            }
        )
    }
}