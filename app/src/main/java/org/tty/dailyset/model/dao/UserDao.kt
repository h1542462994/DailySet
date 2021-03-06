package org.tty.dailyset.model.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.model.entity.User

/**
 * DataAccess to the [org.tty.dailyset.model.entity.User]
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM USER")
    fun all(): Flow<List<User>>

    @Query("SELECT * FROM USER WHERE userUid = :uid LIMIT 1")
    fun load(uid: String): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)
}