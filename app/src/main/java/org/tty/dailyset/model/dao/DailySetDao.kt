package org.tty.dailyset.model.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.data.processor.DailySetProcessor2Async
import org.tty.dailyset.event.DailySetBindingDurationEventArgs
import org.tty.dailyset.event.DailySetCreateDurationAndBindingEventArgs
import org.tty.dailyset.event.DailySetCreateEventArgs
import org.tty.dailyset.localTimestampNow
import org.tty.dailyset.model.converter.DailyDurationTypeConverter
import org.tty.dailyset.model.converter.DailySetTypeConverter
import org.tty.dailyset.model.entity.*

/**
 * the dailySet dao object.
 */
@Dao
@TypeConverters(DailySetTypeConverter::class, DailyDurationTypeConverter::class)
interface DailySetDao: DailySetProcessor2Async, DailyDurationDao, DailySetBindingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailySet: DailySet)
    @Delete
    suspend fun delete(dailySet: DailySet)

    /**
     * insert query.
     */
    @Query("select count(*) from daily_set where type = :type")
    fun countOfType(type: DailySetType): Int

    @Query("select * from daily_set")
    fun allSets(): Flow<List<DailySet>>

    @Query("select * from daily_set where uid = :dailySetUid")
    fun load(dailySetUid: String): Flow<DailySet?>

    @Query("select * from daily_set where uid = :dailySetUid")
    fun loadDetail(dailySetUid: String): Flow<DailySetDurations?>

    @Transaction
    override suspend fun create(dailySetCreateEventArgs: DailySetCreateEventArgs) {
        val (dailySetName, uid, ownerUid, type, icon) = dailySetCreateEventArgs

        val serialIndex = countOfType(type)
        val dailySet = DailySet(
            type = type,
            icon = icon,
            uid = uid,
            serialIndex = serialIndex,
            ownerUid = ownerUid,
            name = dailySetName,
            updateAt = localTimestampNow()
        )
        update(dailySet)
    }

    @Transaction
    override suspend fun createDuration(dailySetCreateDurationAndBindingEventArgs: DailySetCreateDurationAndBindingEventArgs) {
        val (dailyDurationUid, dailySetUid, name, ownerUid, startDate, endDate, periodCode, bindingDailyTableUid) = dailySetCreateDurationAndBindingEventArgs


        val serialIndex = countOfType(DailyDurationType.Clazz)
        val dailyDuration = DailyDuration(
            type = DailyDurationType.Clazz,
            uid = dailyDurationUid,
            ownerUid = ownerUid,
            startDate = startDate,
            endDate = endDate,
            name = name,
            tag = DailyDurationTag.Normal,
            serialIndex = serialIndex,
            bindingPeriodCode = periodCode.code,
            updateAt = localTimestampNow()
        )
        update(dailyDuration)

        bindingDuration(
            DailySetBindingDurationEventArgs(
                dailySetUid = dailySetUid,
                dailyDurationUid = dailyDurationUid,
                bindingDailyTableUid = bindingDailyTableUid
            )
        )
    }

    override suspend fun bindingDuration(dailySetBindingDurationEventArgs: DailySetBindingDurationEventArgs) {
        val (dailySetUid, dailyDurationUid, bindingDailyTableUid) = dailySetBindingDurationEventArgs
        val dailySetBinding = DailySetBinding(
            dailySetUid = dailySetUid,
            dailyDurationUid = dailyDurationUid,
            bindingDailyTableUid = bindingDailyTableUid,
            updateAt = localTimestampNow()
        )
        update(dailySetBinding)

    }
}