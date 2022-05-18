package org.tty.dailyset.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.MainDestination
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.component.common.StatusBarToBackground
import org.tty.dailyset.component.common.asMutableState
import org.tty.dailyset.component.common.showToast
import org.tty.dailyset.component.login.LoginInput
import org.tty.dailyset.component.login.rememberLoginVM
import org.tty.dailyset.ui.component.*
import org.tty.dailyset.ui.image.ImageResource
import org.tty.dailyset.ui.theme.DailySetTheme

@Composable
@UseViewModel("/login")
fun LoginPage(input: LoginInput) {
    StatusBarToBackground()

    val loginVM = rememberLoginVM()

    val userTextState = loginVM.usernameText.asMutableState()
    val passwordTextState = loginVM.passwordText.asMutableState()
    val loginButtonEnabled by loginVM.loginButtonEnabled.collectAsState()
    val userTipValue by loginVM.usernameTipValue.collectAsState()
    val passwordTipValue by loginVM.passwordTipValue.collectAsState()
    val isOnLogin by loginVM.isOnLogin.collectAsState()
    val users by loginVM.users.collectAsState()
    val usersInDropDownList = users.filter {
        !it.localUser
    }
    val nav = LocalNav.current
    // if route is from index, it will not allowed to use back.
    val useBack = input.from != MainDestination.INDEX_ROUTE
    // if route is from user, it will not allowed to change user.
    val inputEnabled = input.from != MainDestination.USER_ROUTE
    // if the login is reLogin.
    val isReLogin = input.from == MainDestination.USER_ROUTE
    val title = if (isReLogin) {
        stringResource(R.string.re_login)
    } else {
        stringResource(R.string.login)
    }

    LaunchedEffect(key1 = input, block = {
        if (input.username.isNotEmpty()) {
            userTextState.value = input.username
        }
    })


    Column {
        CenterBar(useBack = useBack, content = title)
        Spacer(modifier = Modifier.height(128.dp))


        InputFieldWithMore(
            inputValueState = userTextState,
            inputLabel = stringResource(id = R.string.login_user_field),
            inputTip = userTipValue,
            inputPlaceHolder = stringResource(id = R.string.login_user_field_tip),
            inputEnabled = inputEnabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            icon = ImageResource.user(),
            moreVisible = usersInDropDownList.isNotEmpty() && inputEnabled
        ) { callback ->
            usersInDropDownList.forEach {
                key(it.userUid) {
                    DropdownMenuItem(onClick = {
                        userTextState.value = it.userUid
                        callback()
                    }) {
                        UserProfileSmall(user = it)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        PasswordField(
            inputValueState = passwordTextState,
            inputLabel = stringResource(id = R.string.login_password_field),
            inputTip = passwordTipValue,
            inputPlaceHolder = stringResource(id = R.string.login_password_field_tip),
            icon = ImageResource.password()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = stringResource(id = R.string.login_register_action_tip),
                style = DailySetTheme.typography.linkText,
                color = DailySetTheme.color.primaryColor,
                modifier = Modifier
                    .clickable {
                        //showToast("该功能暂未实现。")
                        nav.action.toRegister()
                    }
                    .padding(8.dp)
            )
            Text(
                text = stringResource(id = R.string.login_forget_password_action_tip),
                style = DailySetTheme.typography.linkText,
                modifier = Modifier
                    .clickable {
                        showToast("该功能暂未实现。")
                    }
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        ButtonField(
            text = title,
            onActionText = stringResource(id = R.string.login_on_login),
            isEnabled = loginButtonEnabled,
            isOnAction = isOnLogin,
            onClick = {
                loginVM.loginWithPassword(nav.action, isReLogin)
            }
        )

        Spacer(modifier = Modifier.weight(3.0f))
    }

    // if the input is from index, then intercept the back event.
    if (!useBack) {
        BackHandler {

        }
    }
}

@Preview
@Composable
fun LoginPagePreview() {

    var textValue by remember {
        mutableStateOf("")
    }
    Column {
        CenterBar(content = stringResource(id = R.string.login))

        Row {
            ImageBox(
                modifier = Modifier.align(Alignment.Bottom),
                painter = ImageResource.user()
            )
            TextField(
                value = textValue,
                onValueChange = { textValue = it },
                modifier = Modifier.align(Alignment.CenterVertically),
                label = { Text("用户名") })
        }


    }
}