/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.bean

object ResponseCodes {
    /**
     * success: 0
     */
    const val success: Int = 0

    /**
     * fail: 1
     */
    const val fail: Int = 1

    /**
     * argError: 2
     */
    const val argError: Int = 2

    /**
     * userNotExist: 101
     */
    const val userNoExists: Int = 101

    /**
     * passwordError: 102
     */
    const val passwordError: Int = 102

    /**
     * tokenError: 103
     */
    const val tokenError: Int = 103

    /**
     * deviceCodeError: 104
     */
    const val deviceCodeError: Int = 104
}