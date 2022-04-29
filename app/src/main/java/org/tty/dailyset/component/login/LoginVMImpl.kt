package org.tty.dailyset.component.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.tty.dailyset.annotation.UseComponent
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.common.observable.InitialFlow
import org.tty.dailyset.common.observable.asInitialFlow
import org.tty.dailyset.common.observable.initialMutableSharedFlowOf
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.collectSkipFirst
import org.tty.dailyset.component.common.sharedComponents

/**
 * WARNING: must remember the created viewModel, otherwise it will be created every recomposition.
 */
@Composable
@UseComponent
fun rememberLoginVM(): LoginVM {
    val sharedComponents = sharedComponents()
    return remember { LoginVMImpl(sharedComponents) }
}


class LoginVMImpl(private val sharedComponents: SharedComponents) : LoginVM {
    private val userTipValueInternal = initialMutableSharedFlowOf("")

    override val loginButtonEnabled: InitialFlow<Boolean>
        get() = TODO("Not yet implemented")
    override val userTipValue: InitialFlow<String> = userTipValueInternal.asInitialFlow()
    override val passwordTipValue: InitialFlow<String>
        get() = TODO("Not yet implemented")
    override val isOnLogin: InitialFlow<Boolean>
        get() = TODO("Not yet implemented")

    override val userText: MutableStateFlow<String> = MutableStateFlow("")

    override var userTextInitial: String = ""

    override fun setUserText(value: String) {
        logger.d(TAG, "setUserText: $value")
        userTextInitial = ""
    }

    override var passwordTextInitial: String = ""

    override fun setPasswordText(value: String) {
        logger.d(TAG, "setPasswordText: $value")
        passwordTextInitial = ""
    }

    override fun loginWithPassword(uid: String, password: String) {
        TODO("Not yet implemented")
    }

    fun valid(user: String, password: String): Boolean {
        val userValid = validUserTextField(user)
        return false
    }

    fun validUserTextField(value: String): String? {
        if (value.isEmpty()) {
            return null
        } else {
            val number = value.toIntOrNull() ?: return "用户名必须为数字"

            if (number <= 10000) {
                return "用户名错误"
            }

            return null
        }
    }

    init {
        sharedComponents.applicationScope.launch {
            userText.collectSkipFirst {
                logger.d(TAG, "userText: $it")
            }
        }
    }

    companion object {
        const val TAG = "LoginVMImpl"
    }

}