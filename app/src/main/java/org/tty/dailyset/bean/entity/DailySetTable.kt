package org.tty.dailyset.bean.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "dailyset_table")
data class DailySetTable(
    @PrimaryKey
    @ColumnInfo(name = "source_uid")
    val sourceUid: String,
    val name: String
)