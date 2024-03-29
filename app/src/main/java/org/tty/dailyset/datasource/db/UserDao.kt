package org.tty.dailyset.datasource.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.User

/**
 * DataAccess to the [org.tty.dailyset.bean.entity.User]
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM USER")
    fun all(): Flow<List<User>>

    @Query("SELECT * FROM USER WHERE user_uid = :uid LIMIT 1")
    fun load(uid: String): Flow<User?>

    @Query("SELECT * FROM USER WHERE user_uid = :uid LIMIT 1")
    suspend fun get(uid: String): User?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: User)
}