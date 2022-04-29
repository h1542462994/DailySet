package org.tty.dailyset.component.login

import kotlinx.coroutines.flow.MutableStateFlow
import org.tty.dailyset.common.observable.InitialFlow

interface LoginVM {
    val loginButtonEnabled: InitialFlow<Boolean>
    val userTipValue: InitialFlow<String>
    val passwordTipValue: InitialFlow<String>
    val isOnLogin: InitialFlow<Boolean>

    val userText: MutableStateFlow<String>
    val userTextInitial: String
    fun setUserText(value: String)
    val passwordTextInitial: String
    fun setPasswordText(value: String)

    fun loginWithPassword(uid: String, password: String)
}