/**
 * create at 2022/4/15
 * author h1542462994
 *
 * request bean class UserLoginReq
 */

package org.tty.dailyset.bean.req

import kotlinx.serialization.Serializable

@Serializable
class UserLoginReq(
    val uid: String,
    val password: String,
    val deviceCode: String?,
    val deviceName: String,
    val platformCode: Int
)