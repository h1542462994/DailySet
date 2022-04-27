package org.tty.dailyset.bean.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.tty.dailyset.converter.DailyNodeTypeConverter
import org.tty.dailyset.converter.StringLocalDateTimeConverter
import java.time.LocalDateTime

/**
 * represents a dailyNode
 * a dailyNode is the component of [DailySet]
 */
@Entity(tableName = "daily_node")
@TypeConverters(DailyNodeTypeConverter::class, StringLocalDateTimeConverter::class)
data class DailyNode(
    /**
     * the dailyNode type
     */
    val type: DailyNodeType = DailyNodeType.Normal,
    /**
     * the name of the dailyNode
     */
    val name: String = "",
    /**
     * the dailySetId
     */
    val dailySetId: String = "",
    /**
     * the unique uid
     */
    @PrimaryKey
    val dailyNodeUid: String = "",

    val serialIndex: Int = 0,
    /**
     * the update timestamp
     */
    val updateAt: LocalDateTime
)