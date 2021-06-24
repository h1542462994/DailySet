package org.tty.dailyset.model.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.data.processor.DailySetProcessor2Async
import org.tty.dailyset.event.DailySetCreateEventArgs
import org.tty.dailyset.localTimestampNow
import org.tty.dailyset.model.converter.DailySetTypeConverter
import org.tty.dailyset.model.entity.DailySet
import org.tty.dailyset.model.entity.DailySetType

/**
 * the dailySet dao object.
 */
@Dao
@TypeConverters(DailySetTypeConverter::class)
interface DailySetDao: DailySetProcessor2Async {
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
    fun all(): Flow<List<DailySet>>

    @Query("select * from daily_set where uid = :dailySetUid")
    fun load(dailySetUid: String): Flow<DailySet?>

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
}