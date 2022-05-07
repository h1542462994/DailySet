package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.UserTicketInfo

@Dao
interface UserTicketInfoDao {
    @Delete
    suspend fun remove(userTicketInfo: UserTicketInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(unicTicketInfo: UserTicketInfo)

    @Query("SELECT * FROM user_ticket_info WHERE user_uid = :userUid LIMIT 1")
    suspend fun get(userUid: String): UserTicketInfo?

    @Query("SELECT * FROM user_ticket_info WHERE user_uid = :userUid LIMIT 1")
    fun load(userUid: String): Flow<UserTicketInfo?>


}