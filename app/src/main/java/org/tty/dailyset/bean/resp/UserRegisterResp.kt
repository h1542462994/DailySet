/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.bean.resp

import kotlinx.serialization.Serializable

@Serializable
class UserRegisterResp(
    val uid: Int,
    val nickname: String,
    val email: String,
    val portraitId: String
)