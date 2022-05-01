package org.tty.dailyset.bean.util

/**
 * 判断用户名是否合法
 * @return null 合法
 */
fun validUserTextField(value: String): String? {
    if (value.isEmpty()) {
        return ""
    } else {
        if (value.startsWith('0') || !value.all { it.isDigit() }) {
            return " (用户名必须为数字) "
        }

        val number = value.toIntOrNull()
        if (number == null || number <= 100000 || number > 99999999) {
            return " (无效的用户名) "
        }

        return null
    }
}

/**
 * 判断密码是否合法
 * @return null 合法
 */
fun validPasswordTextField(value: String): String? {
    if (value.isEmpty()) {
        return ""
    } else {
        if (!value.all { it.isDigit() || it.isLetter() || it in "_-.@" }) {
            return " (含有无效字符: 除数字,字母,_-.@) "
        } else if (value.length < 6 || value.length > 64) {
            return " (长度必须在6-64之间) "
        }

        return null
    }
}

fun validNicknameTextField(value: String): String? {
    if (value.isEmpty()) {
        return ""
    } else {
        if (value.length < 2 || value.length > 64) {
            return " (长度必须在2-64之间) "
        }

        return null
    }
}

fun validEmailTextField(value: String): String? {
    if (value.isEmpty()) {
        return ""
    } else {
        if (!value.contains("@")) {
            return " (邮箱格式不正确) "
        } else if (value.length < 6 || value.length > 128) {
            return " (长度必须在6-128之间) "
        }
        return null
    }
}

fun anyTextEmpty(vararg texts: String?): Boolean {
    return texts.any { it.isNullOrEmpty() }
}

fun anyIntEmpty(vararg ints: Int?): Boolean {
    return ints.any { it == null }
}