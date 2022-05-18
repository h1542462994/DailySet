package org.tty.dailyset.component.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.tty.dailyset.MainActions
import org.tty.dailyset.annotation.UseComponent
import org.tty.dailyset.bean.util.validPasswordTextField
import org.tty.dailyset.bean.util.validUserTextField
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.component.common.*

/**
 * WARNING: must remember the created viewModel, otherwise it will be created every recomposition.
 */
@Composable
@UseComponent
fun rememberLoginVM(): LoginVM {
    val sharedComponents = sharedComponents()
    return sharedComponents.ltsVMSaver.getVM("loginVM") {
        LoginVMImpl(sharedComponents)
    }
}


class LoginVMImpl(private val sharedComponents: SharedComponents) : LoginVM {

    override val loginButtonEnabled = MutableStateFlow(false)
    override val usernameTipValue = MutableStateFlow("")
    override val passwordTipValue = MutableStateFlow("")
    override val isOnLogin = MutableStateFlow(false)
    override val usernameText: MutableStateFlow<String> = MutableStateFlow("")
    override val passwordText: MutableStateFlow<String> = MutableStateFlow("")
    override val users = sharedComponents.stateStore.users.asActivityColdStateFlow(listOf())

    override fun loginWithPassword(navAction: MainActions, isReLogin: Boolean) {
        logger.d("LoginVMImpl","called loginWithPassword.")


        sharedComponents.activityScope.launch {
            val uid = usernameText.value
            val password = passwordText.value
            setOnLogin()
            sharedComponents.actorCollection.userActor.login(uid, password, navAction, isReLogin)
            setOnLoginFinished()
        }
    }

    private fun setOnLogin() {
        loginButtonEnabled.value = false
        isOnLogin.value = true
    }

    private fun setOnLoginFinished() {
        isOnLogin.value = false
    }

    private fun valid(user: String, password: String) {
        val userValid = validUserTextField(user)
        usernameTipValue.value = userValid ?: ""
        val passwordValid = validPasswordTextField(password)
        passwordTipValue.value = passwordValid ?: ""
        loginButtonEnabled.value = userValid == null && passwordValid == null
    }

    init {
        usernameText.observeOnApplicationScope {
            valid(it, passwordText.value)
        }
        passwordText.observeOnApplicationScope {
            valid(usernameText.value, it)
        }
    }

    companion object {
        const val TAG = "LoginVMImpl"
    }
}

class LoginVMSaver : Saver<LoginVM, HashMap<String, Any>> {
    override fun restore(value: HashMap<String, Any>): LoginVM {
        val loginVM = LoginVMImpl(sharedComponents())
        loginVM.loginButtonEnabled.value = value["loginButtonEnabled"] as Boolean
        loginVM.usernameText.value = value["userText"] as String
        loginVM.passwordText.value = value["passwordText"] as String
        loginVM.usernameTipValue.value = value["userTipValue"] as String
        loginVM.passwordTipValue.value = value["passwordTipValue"] as String
        return loginVM
    }

    override fun SaverScope.save(value: LoginVM): HashMap<String, Any> {
        return hashMapOf(
            "loginButtonEnabled" to value.loginButtonEnabled.value,
            "userText" to value.usernameText.value,
            "passwordText" to value.passwordText.value,
            "userTipValue" to value.usernameTipValue.value,
            "passwordTipValue" to value.passwordTipValue.value
        )
    }
}