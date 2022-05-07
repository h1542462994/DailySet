package org.tty.dailyset.bean.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable
import org.tty.dailyset.bean.converters.LocalDateConverter
import java.time.LocalDate
import org.tty.dailyset.bean.enums.DailySetDurationType
import org.tty.dailyset.bean.enums.DailySetPeriodCode
import org.tty.dailyset.bean.serializer.LocalDateSerializer

/**
 * represents a long daily duration
 * the daily duration belong to a user <br/>
 * **source_duration(4)**
 */
@Serializable
@Entity(tableName = "dailyset_duration")
@TypeConverters(LocalDateConverter::class)
data class DailySetDuration(
    @PrimaryKey
    @ColumnInfo(name = "source_uid")
    val sourceUid: String,
    /**
     * @see DailySetDurationType
     */
    val type: Int,
    @Serializable(with = LocalDateSerializer::class)
    @ColumnInfo(name = "start_date")
    val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    @ColumnInfo(name = "end_date")
    val endDate: LocalDate,
    val name: String,
    val tag: String,
    @ColumnInfo(name = "binding_year")
    val bindingYear: Int,
    /**
     * @see DailySetPeriodCode
     */
    @ColumnInfo(name = "binding_period_code")
    val bindingPeriodCode: Int
)