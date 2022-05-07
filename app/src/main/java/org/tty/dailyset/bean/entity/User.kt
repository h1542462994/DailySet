package org.tty.dailyset.bean.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.tty.dailyset.bean.enums.PreferenceName

/**
 * 保存当前使用的用户
 */
@Entity(tableName = "user")
data class User (
    @PrimaryKey
    @ColumnInfo(name = "user_uid")
    var userUid: String,
    var name: String,
    @ColumnInfo(name = "nick_name")
    var nickName: String,
    var token: String,
    @ColumnInfo(name = "local_user")
    var localUser: Boolean,
    var state: Int
){
    companion object {
        const val system = "#system"
        const val local = "#LOCAL"

        fun default(): User {
            return User(
                userUid = PreferenceName.CURRENT_USER_UID.defaultValue,
                name = PreferenceName.CURRENT_USER_UID.defaultValue,
                nickName = "本地账户",
                token = "",
                localUser = true,
                state = 0
            )
        }

        fun system(): User {
            return User(
                userUid = system,
                name = system,
                nickName = "系统",
                token = "",
                localUser = false,
                state = -1
            )
        }
    }

}