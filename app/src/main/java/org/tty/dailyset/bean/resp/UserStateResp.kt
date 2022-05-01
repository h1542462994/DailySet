/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.bean.resp

import java.time.LocalDateTime

class UserStateResp(
    val uid: Int,
    val nickname: String,
    val email: String,
    val portraitId: String,
    val deviceCode: String,
    val deviceName: String,
    val platformCode: Int,
    val state: Int,
    val lastActive: LocalDateTime
)