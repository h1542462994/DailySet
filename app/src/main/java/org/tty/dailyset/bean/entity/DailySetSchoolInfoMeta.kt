package org.tty.dailyset.bean.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * **meta-school_info_meta(4)**
 */
@Serializable
@Entity(tableName = "dailyset_school_info_meta")
data class DailySetSchoolInfoMeta(
    @PrimaryKey
    @ColumnInfo(name = "meta_uid")
    val metaUid: String,
    val identifier: String,
    val name: String
)