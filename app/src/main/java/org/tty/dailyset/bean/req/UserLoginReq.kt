/**
 * create at 2022/4/15
 * author h1542462994
 *
 * request bean class UserLoginReq
 */

package org.tty.dailyset.bean.req

import org.tty.dailyset.bean.util.anyIntEmpty
import org.tty.dailyset.bean.util.anyTextEmpty
import org.tty.dailyset.bean.enums.PlatformCode

@kotlinx.serialization.Serializable
class UserLoginReq(
    val uid: Int? = null,
    val password: String? = null,
    val deviceCode: String? = null,
    val deviceName: String? = null,
    val platformCode: Int? = null
) {
    fun verify(): Boolean {
        return !anyIntEmpty(uid, platformCode) && !anyTextEmpty(password, deviceName)
                && PlatformCode.values().any { it.code == platformCode }
    }

}