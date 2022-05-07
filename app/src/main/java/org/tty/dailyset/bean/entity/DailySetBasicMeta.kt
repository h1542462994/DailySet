package org.tty.dailyset.bean.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * **meta-basic_meta(1)**
 */
@Serializable
@Entity(tableName = "dailyset_basic_meta")
data class DailySetBasicMeta(
    @PrimaryKey
    @ColumnInfo(name = "meta_uid")
    val metaUid: String,
    val name: String,
    val icon: String
)