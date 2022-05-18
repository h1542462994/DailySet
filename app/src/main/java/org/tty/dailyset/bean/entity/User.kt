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
    val email: String,
    var token: String,
    @ColumnInfo(name = "local_user")
    var localUser: Boolean,
    var state: Int
)