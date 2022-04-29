package org.tty.dailyset.annotation

import org.tty.dailyset.component.common.SharedComponents

/**
 * marked on **inter functions**, to declare the function will depend on [SharedComponents]
 * @see [org.tty.dailyset.provider.LocalSharedComponents]
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
annotation class UseComponent()
