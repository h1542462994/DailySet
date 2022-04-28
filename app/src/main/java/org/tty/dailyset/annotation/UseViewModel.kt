package org.tty.dailyset.annotation

/**
 *
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
annotation class UseViewModel(val value: String = "")

