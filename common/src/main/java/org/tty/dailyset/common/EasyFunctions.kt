package org.tty.dailyset.common

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