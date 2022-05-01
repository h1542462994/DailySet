/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.bean.resp

@kotlinx.serialization.Serializable
class UserLoginResp(
    val uid: Int,
    val nickname: String,
    val email: String,
    val portraitId: String,
    val token: String,
    val deviceCode: String,
    val deviceName: String,
    val platformCode: Int,
)