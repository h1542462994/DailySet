package org.tty.dailyset.bean.resp

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import org.tty.dailyset.bean.DailySetUpdateItemCollection
import org.tty.dailyset.bean.entity.DailySet

@Serializable
data class DailySetUpdateRawResult(
    val dailySet: DailySet,

    /**
     * updateItems
     * *JsonElement* is the dynamic raw json data.
     */
    val updateItems: List<DailySetUpdateItemCollection<JsonElement>>
)