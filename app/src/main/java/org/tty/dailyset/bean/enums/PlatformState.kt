/**
 * create at 2022/4/17
 * @author h1542462994
 */

package org.tty.dailyset.bean.enums

enum class PlatformState(
    val state: Int
) {
    /**
     * keep-alive state.
     */
    ALIVE(0),

    /**
     * leave state.
     */
    LEAVE(1),

    /**
     * invalid state, user must re-login.
     */
    INVALID(2),

    /**
     * reserved state?.
     */
    BAN(3),
}