package org.tty.dailyset.common.util

import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*

fun uuid(): String {
    return UUID.randomUUID().toString()
}

fun stringToBase64(str: String): String {
    return Base64.getEncoder().encodeToString(str.toByteArray())
}

fun base64ToString(str: String): String {
    return String(Base64.getDecoder().decode(str))
}

fun urlEncode(str: String): String {
    return URLEncoder.encode(str, "UTF-8")
}

fun urlDecode(str: String): String {
    return URLDecoder.decode(str, "UTF-8")
}