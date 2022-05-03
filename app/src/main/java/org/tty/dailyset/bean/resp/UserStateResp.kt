/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.bean.resp

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.tty.dailyset.bean.serializer.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
class UserStateResp(
    val uid: Int,
    val nickname: String,
    val email: String,
    val portraitId: String,
    val deviceCode: String,
    val deviceName: String,
    val platformCode: Int,
    val state: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val lastActive: LocalDateTime,
    val token: String
)