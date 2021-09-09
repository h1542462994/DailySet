package org.tty.dailyset.common

// TODO: migrate to dioc.util

/**
 * optional function.
 */
inline fun <reified T, reified R> T?.optional(crossinline mapper: T.() -> R): R? {
    return if (this == null) {
        null
    } else {
        mapper(this)
    }
}

