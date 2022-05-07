package org.tty.dailyset.bean.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import org.tty.dailyset.bean.UpdatableItemLink
import org.tty.dailyset.bean.converters.LocalDateTimeConverter
import java.time.LocalDateTime
import kotlinx.serialization.Serializable
import org.tty.dailyset.bean.serializer.LocalDateTimeSerializer
import org.tty.dailyset.bean.enums.DailySetMetaType

@Serializable
@Entity(tableName = "dailyset_meta_links", primaryKeys = ["dailyset_uid", "meta_type", "meta_uid"])
@TypeConverters(LocalDateTimeConverter::class)
data class DailySetMetaLinks (
    @ColumnInfo(name = "dailyset_uid")
    val dailySetUid: String,
    /**
     * @see DailySetMetaType
     */
    @ColumnInfo(name = "meta_type")
    val metaType: Int,
    @ColumnInfo(name = "meta_uid")
    val metaUid: String,
    @ColumnInfo(name = "insert_version")
    override val insertVersion: Int,
    @ColumnInfo(name = "update_version")
    override val updateVersion: Int,
    @ColumnInfo(name = "remove_version")
    override val removeVersion: Int,
    @ColumnInfo(name = "last_tick")
    @Serializable(with = LocalDateTimeSerializer::class)
    override val lastTick: LocalDateTime
): UpdatableItemLink