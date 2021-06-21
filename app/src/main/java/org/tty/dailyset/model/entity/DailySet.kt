package org.tty.dailyset.model.entity

import java.sql.Timestamp

/**
 * represents a dailySet
 */
data class DailySet(
    /**
     * the dailySet type.
     */
    val type: DailySetType = DailySetType.Normal,

    /**
     * the dailySet icon
     */
    val icon: DailySetIcon?,

    /**
     * the unique uid.
     */
    val uid: String = "",

    /**
     * the ownerUid
     */
    val ownerUid: String = "",

    /**
     * the display name.
     */
    val name: String = "",

    /**
     * the update timestamp
     */
    val updateAt: Timestamp
)