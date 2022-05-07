package org.tty.dailyset.bean.req

import kotlinx.serialization.Serializable

@Serializable
class UserAutoLoginReq(
    val token: String
)