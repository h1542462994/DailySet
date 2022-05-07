package org.tty.dailyset.bean

import org.tty.dailyset.bean.enums.DailySetDataType
import kotlinx.serialization.Serializable

@Serializable
data class DailySetUpdateItemCollection<T: Any>(
    /**
     * **type** 数据类型 **source | matte | meta**
     * @see DailySetDataType
     */
    val type: Int,

    /**
     * **subType** 子数据类型 **?**
     */
    val subType: Int,
    val updates: List<DailySetUpdateItem<T>>
)
