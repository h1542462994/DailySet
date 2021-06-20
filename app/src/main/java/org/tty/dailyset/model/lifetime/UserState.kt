package org.tty.dailyset.model.lifetime

import org.tty.dailyset.model.entity.User

/**
 * mark the current user state.
 */
data class UserState(
    val currentUser: User?,
    val currentUserUid: String,
)