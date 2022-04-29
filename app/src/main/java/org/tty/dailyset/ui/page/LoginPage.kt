package org.tty.dailyset.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.tty.dailyset.MainDestination
import org.tty.dailyset.R
import org.tty.dailyset.component.common.StatusBarToBackground
import org.tty.dailyset.component.common.rememberAsMutableState
import org.tty.dailyset.component.common.showToast
import org.tty.dailyset.component.login.LoginInput
import org.tty.dailyset.component.login.rememberLoginVM
import org.tty.dailyset.ui.component.CenterBar
import org.tty.dailyset.ui.component.ImageBox
import org.tty.dailyset.ui.image.ImageResource
import org.tty.dailyset.ui.theme.DailySetTheme

@Composable
fun LoginPage(input: LoginInput) {
    StatusBarToBackground()

    val loginVM = rememberLoginVM()
    var userTextField by remember("") { mutableStateOf(loginVM.userTextInitial) }
    fun updateUserText(text: String) {
        userTextField = text
        loginVM.setUserText(text)
    }

    var userText by loginVM.userText.rememberAsMutableState()
    var passwordTextField by remember("") { mutableStateOf(loginVM.passwordTextInitial) }
    fun updatePasswordText(text: String) {
        passwordTextField = text
        loginVM.setPasswordText(text)
    }


    Column {
        CenterBar(content = stringResource(id = R.string.login))
        Spacer(modifier = Modifier.height(128.dp))

        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            OutlinedTextField(
                value = userText,
                onValueChange = {
                    userText = it
//                    logger.d("User", "onValueChange: $it")
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(fraction = 0.7f),
                label = { Text(stringResource(R.string.login_user_field)) },
                placeholder = { Text(stringResource(R.string.login_user_field_tip)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Send
                ),
                maxLines = 1,

                leadingIcon = {
                    ImageBox(
                        painter = ImageResource.user()
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            OutlinedTextField(
                value = passwordTextField,
                onValueChange = { updatePasswordText(it) },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(fraction = 0.7f),
                label = { Text(stringResource(id = R.string.login_user_password)) },
                placeholder = { Text(stringResource(id = R.string.login_user_password_tip)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Send
                ),
                maxLines = 1,
                visualTransformation = PasswordVisualTransformation(mask = '\u273c'),
                leadingIcon = {
                    ImageBox(
                        painter = ImageResource.password()
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = "立即注册",
                style = DailySetTheme.typography.linkText,
                color = DailySetTheme.color.primaryColor,
                modifier = Modifier
                    .clickable {
                        showToast("该功能暂未实现。")
                    }
                    .padding(8.dp)
            )
            Text(
                text = "忘记密码？",
                style = DailySetTheme.typography.linkText,
                modifier = Modifier
                    .clickable {
                        showToast("该功能暂未实现。")
                    }
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(8.dp)
            .fillMaxWidth(0.7f), onClick = { /*TODO*/ }) {
            Text(
                "登录",
                modifier = Modifier.padding(4.dp),
                style = DailySetTheme.typography.buttonText
            )
        }

        Spacer(modifier = Modifier.weight(3.0f))
    }

    // if the input is from index, then intercept the back event.
    if (input.from == MainDestination.INDEX) {
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