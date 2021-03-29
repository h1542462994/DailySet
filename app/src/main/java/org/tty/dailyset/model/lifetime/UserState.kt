package org.tty.dailyset.model.lifetime

import org.tty.dailyset.model.entity.User

data class UserState(
    val currentUser: User?,
    val currentUserUid: String,
)