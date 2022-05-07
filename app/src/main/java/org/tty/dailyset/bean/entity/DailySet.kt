package org.tty.dailyset.bean.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * represents a dailySet
 */
@Serializable
@Entity(tableName = "dailyset")
data class DailySet(
    @PrimaryKey
    val uid: String,
    val type: Int,
    @ColumnInfo(name = "source_version")
    val sourceVersion: Int,
    @ColumnInfo(name = "matte_version")
    val matteVersion: Int,
    @ColumnInfo(name = "meta_version")
    val metaVersion:Int
)