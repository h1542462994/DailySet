package org.tty.dailyset.bean.req

import org.tty.dailyset.bean.resp.DailySetUpdateRawResult
import kotlinx.serialization.Serializable

@Serializable
class DailySetSubmitReq(
    val submitItems: DailySetUpdateRawResult
)