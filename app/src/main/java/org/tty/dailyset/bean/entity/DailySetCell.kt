package org.tty.dailyset.bean.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable
import org.tty.dailyset.bean.converters.LocalTimeConverter
import org.tty.dailyset.bean.serializer.LocalTimeSerializer
import java.time.LocalTime

/**
 * **source-cell(3)**
 */
@Serializable
@Entity(tableName = "dailyset_cell")
@TypeConverters(LocalTimeConverter::class)
data class DailySetCell(
    @PrimaryKey
    @ColumnInfo(name = "source_uid")
    val sourceUid: String,
    @ColumnInfo(name = "row_uid")
    val rowUid: String,
    @ColumnInfo(name = "currentIndex")
    val currentIndex: Int,
    @ColumnInfo(name = "start_time")
    @Serializable(with = LocalTimeSerializer::class)
    val startTime: LocalTime,
    @ColumnInfo(name = "end_time")
    @Serializable(with = LocalTimeSerializer::class)
    val endTime: LocalTime,
    @ColumnInfo(name = "normal_type")
    val normalType: Int,
    @ColumnInfo(name = "serial_index")
    val serialIndex: Int
): DailySetResource