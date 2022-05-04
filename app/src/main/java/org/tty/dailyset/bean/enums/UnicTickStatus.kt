package org.tty.dailyset.bean.enums

enum class UnicTickStatus(val value: Int) {
    NotBind(-1),
    Initialized(0),
    Checked(1),
    UnknownFailure(2),
    PasswordFailure(3)
}