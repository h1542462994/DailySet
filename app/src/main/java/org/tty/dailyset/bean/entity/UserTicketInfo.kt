package org.tty.dailyset.bean.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.tty.dailyset.bean.enums.UnicTickStatus
import org.tty.dailyset.converter.UnicTicketStatusConverter

@Entity(tableName = "user_ticket_info")
@TypeConverters(UnicTicketStatusConverter::class)
data class UserTicketInfo(
    @PrimaryKey
    val userUid: String,
    val status: UnicTickStatus,
    val studentUid: String,
    val departmentName: String,
    val clazzName: String,
    val name: String,
    val grade: Int
) {
    companion object {
        fun empty(): UserTicketInfo {
            return UserTicketInfo(
                userUid = "",
                status = UnicTickStatus.NotBind,
                studentUid = "",
                departmentName = "",
                clazzName = "",
                name = "",
                grade = 0
            )
        }
    }
}