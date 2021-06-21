package org.tty.dailyset.model.entity

import java.sql.Timestamp

/**
 * represents a dailyNode
 * a dailyNode is the component of [DailySet]
 */
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
    val uid: String = "",
    /**
     * the update timestamp
     */
    val updateAt: Timestamp
)