/**
 * create at 2022/4/15
 * author h1542462994
 *
 * request bean class UserRegisterReq
 */

package org.tty.dailyset.bean.req

import kotlinx.serialization.Serializable

@Serializable
class UserRegisterReq(
    val nickname: String,
    val password: String,
    val email: String,
    val portraitId: String? = null,
)