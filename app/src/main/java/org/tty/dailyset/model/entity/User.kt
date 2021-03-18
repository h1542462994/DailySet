package org.tty.dailyset.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

/**
 * 保存当前使用的用户
 */
@Entity(tableName = "user")
data class User (
    @PrimaryKey
    var uid: String,
    var name: String,
    var nickName: String,
    var token: String,
    var localUser: Boolean,
    var state: Int
){
    companion object {
        fun default(): User {
            return User(
                uid = PreferenceName.CURRENT_USER_UID.defaultValue,
                name = PreferenceName.CURRENT_USER_UID.defaultValue,
                nickName = "本地账户",
                token = "",
                localUser = true,
                state = 0
            )
        }
    }

}