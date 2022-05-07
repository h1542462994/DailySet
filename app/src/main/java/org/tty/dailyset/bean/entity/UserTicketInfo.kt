package org.tty.dailyset.bean.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.tty.dailyset.bean.enums.UnicTickStatus
import org.tty.dailyset.bean.converters.UnicTicketStatusConverter

@Entity(tableName = "user_ticket_info")
@TypeConverters(UnicTicketStatusConverter::class)
data class UserTicketInfo(
    @PrimaryKey
    @ColumnInfo(name = "user_uid")
    val userUid: String,
    val status: UnicTickStatus,
    @ColumnInfo(name = "student_uid")
    val studentUid: String,
    @ColumnInfo(name = "department_name")
    val departmentName: String,
    @ColumnInfo(name = "clazz_name")
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