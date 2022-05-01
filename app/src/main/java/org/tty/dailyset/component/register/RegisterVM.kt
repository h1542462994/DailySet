package org.tty.dailyset.component.register

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.MainActions

interface RegisterVM {
    val nicknameText: MutableStateFlow<String>
    val passwordText: MutableStateFlow<String>
    val emailText: MutableStateFlow<String>
    val isOnRegister: StateFlow<Boolean>
    val nicknameTipValue: StateFlow<String>
    val passwordTipValue: StateFlow<String>
    val emailTipValue: StateFlow<String>
    val registerButtonEnabled: StateFlow<Boolean>

    fun registerWithPassword(navAction: MainActions)
}