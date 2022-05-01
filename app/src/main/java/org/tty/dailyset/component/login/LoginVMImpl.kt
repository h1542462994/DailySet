package org.tty.dailyset.component.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tty.dailyset.annotation.UseComponent
import org.tty.dailyset.bean.enums.PlatformCode
import org.tty.dailyset.bean.req.UserLoginReq
import org.tty.dailyset.bean.util.validPasswordTextField
import org.tty.dailyset.bean.util.validUserTextField
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.observeOnApplicationScope
import org.tty.dailyset.component.common.sharedComponents

/**
 * WARNING: must remember the created viewModel, otherwise it will be created every recomposition.
 */
@Composable
@UseComponent
fun rememberLoginVM(): LoginVM {
    val sharedComponents = sharedComponents()
    return rememberSaveable("", saver = LoginVMSaver()) { LoginVMImpl(sharedComponents) }
}


class LoginVMImpl(private val sharedComponents: SharedComponents) : LoginVM {

    override val loginButtonEnabled = MutableStateFlow(false)
    override val userTipValue = MutableStateFlow("")
    override val passwordTipValue = MutableStateFlow("")
    override val isOnLogin = MutableStateFlow(false)
    override val userText: MutableStateFlow<String> = MutableStateFlow("")
    override val passwordText: MutableStateFlow<String> = MutableStateFlow("")

    override fun loginWithPassword() {
        sharedComponents.activityScope.launch {
            val uid = userText.value
            val password = passwordText.value
            setOnLogin()
            sharedComponents.repositoryCollection.userRepository.login(uid, password)
            setOffLogin()
        }
    }

    override fun toRegister() {
        sharedComponents.nav.action.toRegister()
    }

    private fun setOnLogin() {
        loginButtonEnabled.value = false
        isOnLogin.value = true
    }

    private fun setOffLogin() {
        isOnLogin.value = false
    }

    private fun valid(user: String, password: String) {
        val userValid = validUserTextField(user)
        userTipValue.value = userValid ?: ""
        val passwordValid = validPasswordTextField(password)
        passwordTipValue.value = passwordValid ?: ""
        loginButtonEnabled.value = userValid == null && passwordValid == null
    }

    init {
        userText.observeOnApplicationScope {
            valid(it, passwordText.value)
        }
        passwordText.observeOnApplicationScope {
            valid(userText.value, it)
        }
    }

    companion object {
        const val TAG = "LoginVMImpl"
    }

}

class LoginVMSaver: Saver<LoginVM, HashMap<String, Any>> {
    override fun restore(value: HashMap<String, Any>): LoginVM {
        val loginVM = LoginVMImpl(sharedComponents())
        loginVM.loginButtonEnabled.value = value["loginButtonEnabled"] as Boolean
        loginVM.userText.value = value["userText"] as String
        loginVM.passwordText.value = value["passwordText"] as String
        loginVM.userTipValue.value = value["userTipValue"] as String
        loginVM.passwordTipValue.value = value["passwordTipValue"] as String
        return loginVM
    }

    override fun SaverScope.save(value: LoginVM): HashMap<String, Any> {
        return hashMapOf(
            "loginButtonEnabled" to value.loginButtonEnabled.value,
            "userText" to value.userText.value,
            "passwordText" to value.passwordText.value,
            "userTipValue" to value.userTipValue.value,
            "passwordTipValue" to value.passwordTipValue.value
        )
    }
}