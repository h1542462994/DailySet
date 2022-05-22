package org.tty.dailyset.bean.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "dailyset_row")

data class DailySetRow(
    @PrimaryKey
    @ColumnInfo(name = "source_uid")
    val sourceUid: String,
    @ColumnInfo(name = "table_uid")
    val tableUid: String,
    @ColumnInfo(name = "current_index")
    val currentIndex: Int,
    val weekdays: String,
    val counts: String
): DailySetResource