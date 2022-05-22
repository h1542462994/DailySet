package org.tty.dailyset.bean.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "dailyset_student_info_meta")
data class DailySetStudentInfoMeta(
    @PrimaryKey
    @ColumnInfo(name = "meta_uid")
    val metaUid: String,
    @ColumnInfo(name = "department_name")
    val departmentName: String,
    @ColumnInfo(name = "class_name")
    val className: String,
    val name: String,
    val grade: Int
): DailySetResource