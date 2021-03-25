package org.tty.dailyset.ui.utils

import java.sql.Time

fun Time.toShortString(): String {
    return this.toString().substring(0,5)
}