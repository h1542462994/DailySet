package org.tty.dailyset.component.login

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface LoginVM {
    val loginButtonEnabled: StateFlow<Boolean>
    val userTipValue: StateFlow<String>
    val passwordTipValue: StateFlow<String>
    val isOnLogin: StateFlow<Boolean>
    val userText: MutableStateFlow<String>
    val passwordText: MutableStateFlow<String>

    fun loginWithPassword(uid: String, password: String)
    fun toRegister()

}