package org.tty.dailyset.bean.req

import org.tty.dailyset.bean.util.anyTextEmpty
import kotlinx.serialization.Serializable

@Serializable
class UserAutoLoginReq(
    val token: String? = null
) {
    fun verify(): Boolean {
        return !anyTextEmpty(token)
    }
}