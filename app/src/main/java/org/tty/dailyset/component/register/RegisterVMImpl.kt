package org.tty.dailyset.component.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.tty.dailyset.MainActions
import org.tty.dailyset.annotation.UseComponent
import org.tty.dailyset.bean.util.validEmailTextField
import org.tty.dailyset.bean.util.validNicknameTextField
import org.tty.dailyset.bean.util.validPasswordTextField
import org.tty.dailyset.common.observable.flow3
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.observeOnApplicationScope
import org.tty.dailyset.component.common.sharedComponents

@Composable
@UseComponent
fun rememberRegisterVM(): RegisterVM {
    val sharedComponents = sharedComponents()
    return rememberSaveable("", saver = RegisterVMSaver()) {
        RegisterVMImpl(sharedComponents)
    }
}

class RegisterVMImpl(private val sharedComponents: SharedComponents) : RegisterVM {
    override val nicknameText: MutableStateFlow<String> = MutableStateFlow("")
    override val passwordText: MutableStateFlow<String> = MutableStateFlow("")
    override val emailText: MutableStateFlow<String> = MutableStateFlow("")
    override val isOnRegister = MutableStateFlow(false)
    override val nicknameTipValue = MutableStateFlow("")
    override val passwordTipValue = MutableStateFlow("")
    override val emailTipValue = MutableStateFlow("")
    override val registerButtonEnabled = MutableStateFlow(false)

    private fun valid(nickname: String, password: String, email: String) {
        val nicknameValid = validNicknameTextField(nickname)
        nicknameTipValue.value = nicknameValid ?: ""
        val passwordValid = validPasswordTextField(password)
        passwordTipValue.value = passwordValid ?: ""
        val emailValid = validEmailTextField(email)
        emailTipValue.value = emailValid ?: ""
        registerButtonEnabled.value =
            listOf(nicknameValid, passwordValid, emailValid).all { it == null }
    }

    override fun registerWithPassword(navAction: MainActions) {
        sharedComponents.activityScope.launch {
            val nickname = nicknameText.value
            val password = passwordText.value
            val email = emailText.value
            setOnRegister()
            sharedComponents.actorCollection.userActor.register(nickname, password, email, navAction)
            setOnRegisterFinish()
        }
    }

    private fun setOnRegister(){
        registerButtonEnabled.value = false
        isOnRegister.value = true
    }

    private fun setOnRegisterFinish(){
        isOnRegister.value = false
    }

    init {
        val group = flow3(nicknameText, passwordText, emailText) { nickname, password, email ->
            Triple(nickname, password, email)
        }
        group.observeOnApplicationScope { (nickname, password, email) ->
            valid(nickname, password, email)
        }
    }

}

class RegisterVMSaver : Saver<RegisterVM, HashMap<String, Any>> {
    override fun restore(value: HashMap<String, Any>): RegisterVM {
        val registerVM = RegisterVMImpl(sharedComponents())
        registerVM.nicknameText.value = value["nicknameText"] as String
        registerVM.passwordText.value = value["passwordText"] as String
        registerVM.emailText.value = value["emailText"] as String
        registerVM.isOnRegister.value = value["isOnRegister"] as Boolean
        registerVM.nicknameTipValue.value = value["nicknameTipValue"] as String
        registerVM.passwordTipValue.value = value["passwordTipValue"] as String
        registerVM.emailTipValue.value = value["emailTipValue"] as String
        registerVM.registerButtonEnabled.value = value["registerButtonEnabled"] as Boolean
        return registerVM
    }

    override fun SaverScope.save(value: RegisterVM): HashMap<String, Any> {
        return hashMapOf(
            "nicknameText" to value.nicknameText.value,
            "passwordText" to value.passwordText.value,
            "emailText" to value.emailText.value,
            "isOnRegister" to value.isOnRegister.value,
            "nicknameTipValue" to value.nicknameTipValue.value,
            "passwordTipValue" to value.passwordTipValue.value,
            "emailTipValue" to value.emailTipValue.value,
            "registerButtonEnabled" to value.registerButtonEnabled.value
        )
    }

}