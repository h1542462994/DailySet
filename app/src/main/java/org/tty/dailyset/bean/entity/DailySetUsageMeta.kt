package org.tty.dailyset.bean.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "dailyset_usage_meta")
data class DailySetUsageMeta(
    @PrimaryKey
    @ColumnInfo(name = "meta_uid")
    val metaUid: String,
    @ColumnInfo(name = "dailyset_uid")
    val dailySetUid: String,
    @ColumnInfo(name = "user_uid")
    val userUid: String,
    @ColumnInfo(name = "auth_type")
    val authType: Int
): DailySetResource