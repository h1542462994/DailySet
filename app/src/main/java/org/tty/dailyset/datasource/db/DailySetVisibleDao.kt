package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Query
import org.tty.dailyset.bean.entity.DailySetVisible

@Dao
interface DailySetVisibleDao {
    @Query("select * from dailyset_visible where user_uid = :userUid")
    suspend fun allByUserUid(userUid: String): List<DailySetVisible>
}