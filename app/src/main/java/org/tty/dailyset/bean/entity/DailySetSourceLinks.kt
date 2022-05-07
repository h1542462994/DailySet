package org.tty.dailyset.bean.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import org.tty.dailyset.bean.UpdatableItemLink
import java.time.LocalDateTime
import kotlinx.serialization.Serializable
import org.tty.dailyset.bean.converters.LocalDateTimeConverter
import org.tty.dailyset.bean.enums.DailySetSourceType
import org.tty.dailyset.bean.serializer.LocalDateTimeSerializer

@Serializable
@Entity(tableName = "dailyset_source_links", primaryKeys = ["dailyset_uid", "source_type", "source_uid"])
@TypeConverters(LocalDateTimeConverter::class)
data class DailySetSourceLinks (

    @ColumnInfo(name = "dailyset_uid")
    val dailySetUid: String,
    /**
     * @see DailySetSourceType
     */
    @ColumnInfo(name = "source_type")
    val sourceType: Int,
    @ColumnInfo(name = "source_uid")
    val sourceUid: String,
    @ColumnInfo(name = "insert_version")
    override val insertVersion: Int,
    @ColumnInfo(name = "update_version")
    override val updateVersion: Int,
    @ColumnInfo(name = "remove_version")
    override val removeVersion: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    @ColumnInfo(name = "last_tick")
    override val lastTick: LocalDateTime
): UpdatableItemLink