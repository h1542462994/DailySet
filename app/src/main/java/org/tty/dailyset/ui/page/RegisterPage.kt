package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.component.common.StatusBarToBackground
import org.tty.dailyset.component.common.asMutableState
import org.tty.dailyset.component.register.rememberRegisterVM
import org.tty.dailyset.ui.component.ButtonField
import org.tty.dailyset.ui.component.CenterBar
import org.tty.dailyset.ui.component.InputField
import org.tty.dailyset.ui.component.PasswordField
import org.tty.dailyset.ui.image.ImageResource

@Composable
@UseViewModel("/register")
fun RegisterPage() {
    StatusBarToBackground()

    //region register state declarations.
    val registerVM = rememberRegisterVM()

    val nicknameTextState = registerVM.nicknameText.asMutableState()
    val passwordTextState = registerVM.passwordText.asMutableState()
    val emailTextState = registerVM.emailText.asMutableState()
    val registerButtonEnabled by registerVM.registerButtonEnabled.collectAsState()
    val nicknameTipValue by registerVM.nicknameTipValue.collectAsState()
    val passwordTipValue by registerVM.passwordTipValue.collectAsState()
    val emailTipValue by registerVM.emailTipValue.collectAsState()
    val isOnRegister by registerVM.isOnRegister.collectAsState()
    val nav = LocalNav.current
    //endregion

    Column {
        CenterBar(
            useBack = true,
            content = stringResource(id = R.string.register),
            onBackPressed = {
                nav.action.upPress()
            }
        )

        Spacer(modifier = Modifier.height(128.dp))
        InputField(
            inputValueState = nicknameTextState,
            inputLabel = stringResource(R.string.login_nickname_field),
            inputTip = nicknameTipValue,
            inputPlaceHolder = stringResource(R.string.login_nickname_field_tip),
            icon = ImageResource.nickname()
        )

        Spacer(modifier = Modifier.height(8.dp))
        InputField(
            inputValueState = emailTextState,
            inputLabel = stringResource(R.string.login_email_field),
            inputTip = emailTipValue,
            inputPlaceHolder = stringResource(R.string.login_email_field_tip),
            icon = ImageResource.email(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        Spacer(modifier = Modifier.height(8.dp))
        PasswordField(
            inputValueState = passwordTextState,
            inputLabel = stringResource(id = R.string.login_password_field),
            inputTip = passwordTipValue,
            inputPlaceHolder = stringResource(id = R.string.login_password_field_tip),
            icon = ImageResource.password()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ButtonField(
            text = stringResource(id = R.string.register),
            onActionText = stringResource(id = R.string.login_on_register),
            isOnAction = isOnRegister,
            isEnabled = registerButtonEnabled,
        ) {
            registerVM.registerWithPassword(nav.action)
        }


        Spacer(modifier = Modifier.weight(1.0f))
    }


}