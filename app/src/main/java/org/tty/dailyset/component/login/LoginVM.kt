package org.tty.dailyset.component.login

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.MainActions
import org.tty.dailyset.bean.entity.User

interface LoginVM {
    val loginButtonEnabled: StateFlow<Boolean>
    val usernameTipValue: StateFlow<String>
    val passwordTipValue: StateFlow<String>
    val isOnLogin: StateFlow<Boolean>
    val usernameText: MutableStateFlow<String>
    val passwordText: MutableStateFlow<String>
    val users: StateFlow<List<User>>
    fun loginWithPassword(navAction: MainActions, isReLogin: Boolean)

}