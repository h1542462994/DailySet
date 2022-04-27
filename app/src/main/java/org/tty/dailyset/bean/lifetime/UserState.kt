package org.tty.dailyset.bean.lifetime

import org.tty.dailyset.bean.entity.User

/**
 * mark the current user state.
 */
data class UserState(
    val currentUser: User?,
    val currentUserUid: String,
)