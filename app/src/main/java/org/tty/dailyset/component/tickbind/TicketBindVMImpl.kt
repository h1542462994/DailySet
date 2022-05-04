package org.tty.dailyset.component.tickbind

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.tty.dailyset.MainActions
import org.tty.dailyset.bean.util.validPasswordTextField
import org.tty.dailyset.bean.util.validStudentUidTextField
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.observeOnApplicationScope
import org.tty.dailyset.component.common.sharedComponents


@Composable
fun rememberTicketBindVM(): TicketBindVM {
    val sharedComponents = sharedComponents()
    return sharedComponents.ltsVMSaver.getVM("ticketBindVM") {
        TicketBindVMImpl(sharedComponents)
    }
}

class TicketBindVMImpl(private val sharedComponents: SharedComponents): TicketBindVM {
    override val ticketBindButtonEnabled = MutableStateFlow(false)
    override val studentUidText: MutableStateFlow<String> = MutableStateFlow("")
    override val passwordText: MutableStateFlow<String> = MutableStateFlow("")
    override val studentUidTipValue = MutableStateFlow("")
    override val passwordTipValue = MutableStateFlow("")
    override val isOnTicketBind = MutableStateFlow(false)

    override fun bindTicket(navAction: MainActions) {
        sharedComponents.activityScope.launch {
            val studentUid = studentUidText.value
            val password = passwordText.value
            sharedComponents.repositoryCollection.userRepository.bindTicket(studentUid, password, navAction)
        }
    }

    private fun valid(studentUid: String, password: String) {
        val studentUidValid = validStudentUidTextField(studentUid)
        studentUidTipValue.value = studentUidValid ?: ""
        val passwordValid = validPasswordTextField(password)
        passwordTipValue.value = passwordValid ?: ""
        setOnTicketBind(true)
        ticketBindButtonEnabled.value = listOf(studentUidValid, passwordValid).all { it == null }
        setOnTicketBind(false)
    }

    private fun setOnTicketBind(isOnTicketBind: Boolean) {
        this.isOnTicketBind.value = isOnTicketBind
    }

    init {
        studentUidText.observeOnApplicationScope {
            valid(it, passwordText.value)
        }
        passwordText.observeOnApplicationScope {
            valid(studentUidText.value, it)
        }
    }


}