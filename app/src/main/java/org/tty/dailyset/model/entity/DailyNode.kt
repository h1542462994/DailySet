package org.tty.dailyset.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.tty.dailyset.model.converter.DailyNodeTypeConverter
import org.tty.dailyset.model.converter.LongTimeStampConverter
import java.sql.Timestamp

/**
 * represents a dailyNode
 * a dailyNode is the component of [DailySet]
 */
@Entity(tableName = "daily_node")
@TypeConverters(DailyNodeTypeConverter::class, LongTimeStampConverter::class)
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
    val updateAt: Timestamp
)