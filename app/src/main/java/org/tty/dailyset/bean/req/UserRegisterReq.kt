/**
 * create at 2022/4/15
 * author h1542462994
 *
 * request bean class UserRegisterReq
 */

package org.tty.dailyset.bean.req

import org.tty.dailyset.bean.util.anyTextEmpty
import kotlinx.serialization.Serializable

@Serializable
class UserRegisterReq(
    val nickname: String? = null,
    val password: String? = null,
    val email: String? = null,
    val portraitId: String? = null,
) {
    fun verify(): Boolean =
        !anyTextEmpty(nickname, password, email)
}