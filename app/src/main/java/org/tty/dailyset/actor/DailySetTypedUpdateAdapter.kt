package org.tty.dailyset.actor

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import org.tty.dailyset.bean.DailySetUpdateItem
import org.tty.dailyset.bean.DailySetUpdateItemCollection
import org.tty.dailyset.bean.entity.*
import org.tty.dailyset.bean.enums.DailySetDataType
import org.tty.dailyset.bean.enums.DailySetMetaType
import org.tty.dailyset.bean.enums.DailySetSourceType
import org.tty.dailyset.common.util.Diff
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.datasource.UpdatableResourceDao
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class DailySetTypedUpdateAdapter(
    private val sharedComponents: SharedComponents
) {
    suspend fun withUpdateItem(dailySet: DailySet, rawUpdateItemCollection: DailySetUpdateItemCollection<JsonElement>) {
        val realUpdateItem = castToRealType(rawUpdateItemCollection)
        withRealUpdateItem(dailySet, realUpdateItem)
    }

    private data class UpdateType(
        val type: Int,
        val subType: Int
    )

    private fun castToRealType(rawUpdateItemCollection: DailySetUpdateItemCollection<JsonElement>): DailySetUpdateItemCollection<DailySetResource> {
        val updateType = UpdateType(rawUpdateItemCollection.type, rawUpdateItemCollection.subType)
        return DailySetUpdateItemCollection(
            type = updateType.type,
            subType = updateType.subType,
            updates = rawUpdateItemCollection.updates.map {
                DailySetUpdateItem(
                    insertVersion = it.insertVersion,
                    updateVersion = it.updateVersion,
                    removeVersion = it.removeVersion,
                    lastTick = it.lastTick,
                    data = Json.decodeFromJsonElement(serializer(updateType.findType()), it.data) as DailySetResource
                )
            }
        )
    }

    private suspend fun withRealUpdateItem(dailySet: DailySet, itemCollection: DailySetUpdateItemCollection<*>) {
        when (itemCollection.type) {
            DailySetDataType.Source.value -> {
                withRealSourceUpdateItem(dailySet, itemCollection)
            }
            DailySetDataType.Meta.value -> {
                withRealMetaUpdateItem(dailySet, itemCollection)
            }
            else -> {
                error("not supported.")
            }
        }
    }

    private suspend fun withRealSourceUpdateItem(dailySet: DailySet, itemCollection: DailySetUpdateItemCollection<*>) {
        val updateType = UpdateType(itemCollection.type, itemCollection.subType)
        val targetLinks = itemCollection.updates.map {
            DailySetSourceLinks(
                dailySetUid = dailySet.uid,
                sourceType = itemCollection.subType,
                sourceUid = updateType.findUidRetrieval(it.data),
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick
            )
        }
        val sourceLinks = sharedComponents.database.dailySetSourceLinksDao().allBySetUidAndSourceType(
            dailySetUid = dailySet.uid,
            sourceType = itemCollection.subType
        )
        // get diffs
        val diff = Diff<DailySetSourceLinks, DailySetSourceLinks, String> {
            source = sourceLinks
            target = targetLinks
            sourceKeySelector = { it.sourceUid }
            targetKeySelector = { it.sourceUid }
        }
        // with same, update with targets
        sharedComponents.database.dailySetSourceLinksDao().updateBatch(diff.sames.map { it.target })
        // with adds, update
        sharedComponents.database.dailySetSourceLinksDao().updateBatch(diff.adds)

        // update the resource
        updateType.findUpdater().second.updateBatch(
            items = itemCollection.updates.map { it.data }
        )
    }

    private suspend fun withRealMetaUpdateItem(dailySet: DailySet, itemCollection: DailySetUpdateItemCollection<*>) {
        val updateType = UpdateType(itemCollection.type, itemCollection.subType)
        val targetLinks = itemCollection.updates.map {
            DailySetMetaLinks(
                dailySetUid = dailySet.uid,
                metaType = itemCollection.subType,
                metaUid = updateType.findUidRetrieval(it.data),
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick
            )
        }
        val sourceLinks = sharedComponents.database.dailySetMetaLinksDao().allBySetUidAndMetaType(
            dailySetUid = dailySet.uid,
            metaType = itemCollection.subType
        )
        // get diffs
        val diff = Diff<DailySetMetaLinks, DailySetMetaLinks, String> {
            source = sourceLinks
            target = targetLinks
            sourceKeySelector = { it.metaUid }
            targetKeySelector = { it.metaUid }
        }
        // with removes, remove
        sharedComponents.database.dailySetMetaLinksDao().removeBatch(diff.removes)
        // with same, update with targets
        sharedComponents.database.dailySetMetaLinksDao().updateBatch(diff.sames.map { it.target })
        // with adds, update
        sharedComponents.database.dailySetMetaLinksDao().updateBatch(diff.adds)

        // update the resource
        updateType.findUpdater().second.updateBatch(
            items = itemCollection.updates.map { it.data }
        )
    }

    private val updateTypes: List<UpdateType> = listOf(
        UpdateType(DailySetDataType.Source.value, DailySetSourceType.Table.value),
        UpdateType(DailySetDataType.Source.value, DailySetSourceType.Row.value),
        UpdateType(DailySetDataType.Source.value, DailySetSourceType.Course.value),
        UpdateType(DailySetDataType.Source.value, DailySetSourceType.Duration.value),
        UpdateType(DailySetDataType.Source.value, DailySetSourceType.Course.value),
        UpdateType(DailySetDataType.Meta.value, DailySetMetaType.BasicMeta.value),
        UpdateType(DailySetDataType.Meta.value, DailySetMetaType.UsageMeta.value),
        UpdateType(DailySetDataType.Meta.value, DailySetMetaType.SchoolMeta.value),
        UpdateType(DailySetDataType.Meta.value, DailySetMetaType.StudentInfoMeta.value)
    )


    private fun UpdateType.findType(): KType {
        return when(this) {
            UpdateType(DailySetDataType.Source.value, DailySetSourceType.Table.value) -> typeOf<DailySetTable>()
            UpdateType(DailySetDataType.Source.value, DailySetSourceType.Row.value) -> typeOf<DailySetRow>()
            UpdateType(DailySetDataType.Source.value, DailySetSourceType.Cell.value) -> typeOf<DailySetCell>()
            UpdateType(DailySetDataType.Source.value, DailySetSourceType.Duration.value) -> typeOf<DailySetDuration>()
            UpdateType(DailySetDataType.Source.value, DailySetSourceType.Course.value) -> typeOf<DailySetCourse>()
            UpdateType(DailySetDataType.Meta.value, DailySetMetaType.BasicMeta.value) -> typeOf<DailySetBasicMeta>()
            UpdateType(DailySetDataType.Meta.value, DailySetMetaType.UsageMeta.value) -> typeOf<DailySetUsageMeta>()
            UpdateType(DailySetDataType.Meta.value, DailySetMetaType.SchoolMeta.value) -> typeOf<DailySetSchoolInfoMeta>()
            UpdateType(DailySetDataType.Meta.value, DailySetMetaType.StudentInfoMeta.value) -> typeOf<DailySetStudentInfoMeta>()
            else -> error("not supported.")
        }
    }

    private val updaters: Map<UpdateType, UpdatableResourceDao> = mapOf(
        *updateTypes.map { it.findUpdater() }.toTypedArray()
    )

    @Suppress("UNCHECKED_CAST")
    private fun UpdateType.findUpdater(): Pair<UpdateType, UpdatableResourceDao> {
        val dao = when (this) {
            UpdateType(DailySetDataType.Source.value, DailySetSourceType.Table.value) -> {
                object: UpdatableResourceDao {
                    override suspend fun update(item: Any) {
                        sharedComponents.database.dailySetTableDao().update(item as DailySetTable)
                    }

                    override suspend fun updateBatch(items: List<*>) {
                        sharedComponents.database.dailySetTableDao().updateBatch(items as List<DailySetTable>)
                    }
                }
            }
            UpdateType(DailySetDataType.Source.value, DailySetSourceType.Row.value) -> {
                object: UpdatableResourceDao {
                    override suspend fun update(item: Any) {
                        sharedComponents.database.dailySetRowDao().update(item as DailySetRow)
                    }

                    override suspend fun updateBatch(items: List<*>) {
                        sharedComponents.database.dailySetRowDao().updateBatch(items as List<DailySetRow>)
                    }
                }
            }
            UpdateType(DailySetDataType.Source.value, DailySetSourceType.Cell.value) -> {
                object: UpdatableResourceDao {
                    override suspend fun update(item: Any) {
                        sharedComponents.database.dailySetCellDao().update(item as DailySetCell)
                    }

                    override suspend fun updateBatch(items: List<*>) {
                        sharedComponents.database.dailySetCellDao().updateBatch(items as List<DailySetCell>)
                    }
                }
            }
            UpdateType(DailySetDataType.Source.value, DailySetSourceType.Duration.value) -> {
                object: UpdatableResourceDao {
                    override suspend fun update(item: Any) {
                        sharedComponents.database.dailySetDurationDao().update(item as DailySetDuration)
                    }

                    override suspend fun updateBatch(items: List<*>) {
                        sharedComponents.database.dailySetDurationDao().updateBatch(items as List<DailySetDuration>)
                    }
                }
            }
            UpdateType(DailySetDataType.Source.value, DailySetSourceType.Course.value) -> {
                object: UpdatableResourceDao {
                    override suspend fun update(item: Any) {
                        sharedComponents.database.dailySetCourseDao().update(item as DailySetCourse)
                    }

                    override suspend fun updateBatch(items: List<*>) {
                        sharedComponents.database.dailySetCourseDao().updateBatch(items as List<DailySetCourse>)
                    }
                }
            }
            UpdateType(DailySetDataType.Meta.value, DailySetMetaType.BasicMeta.value) -> {
                object: UpdatableResourceDao {
                    override suspend fun update(item: Any) {
                        sharedComponents.database.dailySetBasicMetaDao().update(item as DailySetBasicMeta)
                    }

                    override suspend fun updateBatch(items: List<*>) {
                        sharedComponents.database.dailySetBasicMetaDao().updateBatch(items as List<DailySetBasicMeta>)
                    }
                }
            }
            UpdateType(DailySetDataType.Meta.value, DailySetMetaType.UsageMeta.value) -> {
                object: UpdatableResourceDao {
                    override suspend fun update(item: Any) {
                        sharedComponents.database.dailySetUsageMetaDao().update(item as DailySetUsageMeta)
                    }

                    override suspend fun updateBatch(items: List<*>) {
                        sharedComponents.database.dailySetUsageMetaDao().updateBatch(items as List<DailySetUsageMeta>)
                    }
                }
            }
            UpdateType(DailySetDataType.Meta.value, DailySetMetaType.SchoolMeta.value) -> {
                object: UpdatableResourceDao {
                    override suspend fun update(item: Any) {
                        sharedComponents.database.dailySetSchoolInfoMetaDao().update(item as DailySetSchoolInfoMeta)
                    }

                    override suspend fun updateBatch(items: List<*>) {
                        sharedComponents.database.dailySetSchoolInfoMetaDao().updateBatch(items as List<DailySetSchoolInfoMeta>)
                    }
                }
            }
            UpdateType(DailySetDataType.Meta.value, DailySetMetaType.StudentInfoMeta.value) -> {
                object: UpdatableResourceDao {
                    override suspend fun update(item: Any) {
                        sharedComponents.database.dailySetStudentInfoMetaDao().update(item as DailySetStudentInfoMeta)
                    }

                    override suspend fun updateBatch(items: List<*>) {
                        sharedComponents.database.dailySetStudentInfoMetaDao().updateBatch(items as List<DailySetStudentInfoMeta>)
                    }
                }
            }


            else -> {
                error("not supported.")
            }
        }
        return Pair(this, dao)
    }

    private fun UpdateType.findUidRetrieval(entity: Any): String {
        when (this.findType()) {
            typeOf<DailySetTable>() -> {
                return (entity as DailySetTable).sourceUid
            }
            typeOf<DailySetRow>() -> {
                return (entity as DailySetRow).sourceUid
            }
            typeOf<DailySetCell>() -> {
                return (entity as DailySetCell).sourceUid
            }
            typeOf<DailySetDuration>() -> {
                return (entity as DailySetDuration).sourceUid
            }
            typeOf<DailySetCourse>() -> {
                return (entity as DailySetCourse).sourceUid
            }
            typeOf<DailySetBasicMeta>() -> {
                return (entity as DailySetBasicMeta).metaUid
            }
            typeOf<DailySetUsageMeta>() -> {
                return (entity as DailySetUsageMeta).metaUid
            }
            typeOf<DailySetSchoolInfoMeta>() -> {
                return (entity as DailySetSchoolInfoMeta).metaUid
            }
            typeOf<DailySetStudentInfoMeta>() -> {
                return (entity as DailySetStudentInfoMeta).metaUid
            }
            else -> error("not supported.")
        }
    }
}