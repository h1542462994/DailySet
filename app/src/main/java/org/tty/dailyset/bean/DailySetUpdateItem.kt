package org.tty.dailyset.bean

import kotlinx.serialization.Serializable
import org.tty.dailyset.bean.serializer.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
class DailySetUpdateItem<T: Any>(
    override val insertVersion: Int,
    override val updateVersion: Int,
    override val removeVersion: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val lastTick: LocalDateTime,
    val data: T
): UpdatableItemLink