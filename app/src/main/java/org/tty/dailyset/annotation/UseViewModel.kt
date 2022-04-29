package org.tty.dailyset.annotation

import org.tty.dailyset.component.common.BaseVM

/**
 * marked on **composable pages**, to declare the page use [BaseVM] and it child interfaces.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
annotation class UseViewModel(val value: String = "")

