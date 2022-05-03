package org.tty.dailyset.bean.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_ticket_info")
data class UserTicketInfo(
    @PrimaryKey
    val userUid: String,
    val status: Int,
    val studentUid: String,
    val departmentName: String,
    val clazzName: String,
    val name: String,
    val grade: String,
)