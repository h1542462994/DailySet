/**
 * author: h1542462994
 * response entity used in project.
 */

package org.tty.dailyset.bean

import kotlinx.serialization.Serializable

/**
 * data class of response
 */
@Serializable
data class Responses<T>(
    val code: Int, // data
    val message: String, // message
    val data: T? // data
) {
    companion object {
        /**
         * default fail response entity.
         */
        fun <T> fail(code: Int = ResponseCodes.fail, message: String = "内部错误", data: T? = null): Responses<T>
            = Responses(code, message, data)

        /**
         * default success response entity.
         */
        fun <T> ok(code: Int = ResponseCodes.success, message: String = "请求成功", data: T? = null): Responses<T>
            = Responses(code, message, data)

        /**
         * default fail response entity in arg error.
         */
        fun <T> argError(code: Int = ResponseCodes.argError, message: String = "参数错误", data: T? = null): Responses<T>
            = Responses(code, message, data)

        /**
         * user no exist response entity.
         */
        fun <T> userNoExist(): Responses<T> {
            return fail(ResponseCodes.userNoExists, "用户不存在")
        }

        /**
         * password error response entity.
         */
        fun <T> passwordError(): Responses<T> {
            return fail(ResponseCodes.passwordError, "密码错误")
        }

        /**
         * token error response entity.
         */
        fun <T> tokenError(): Responses<T> {
            return fail(ResponseCodes.tokenError, "token错误")
        }

        /**
         * device code error response entity.
         */
        fun <T> deviceCodeError(): Responses<T> {
            return fail(ResponseCodes.deviceCodeError, "当前设备不存在")
        }
    }
}