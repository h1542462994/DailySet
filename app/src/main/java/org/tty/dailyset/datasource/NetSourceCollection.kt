package org.tty.dailyset.datasource

import org.tty.dailyset.component.common.SuspendInit
import org.tty.dailyset.datasource.net.UserService

interface NetSourceCollection: SuspendInit {
    val userService: UserService
}