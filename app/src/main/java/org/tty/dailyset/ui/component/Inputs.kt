package org.tty.dailyset.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.tty.dailyset.common.observable.state
import org.tty.dailyset.ui.image.ImageResource
import org.tty.dailyset.ui.theme.DailySetTheme
import org.tty.dailyset.ui.theme.LocalPalette

@Composable
fun ColumnScope.InputField(
    inputValueState: MutableState<String>,
    inputLabel: String,
    inputTip: String,
    inputPlaceHolder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Text
    ),
    icon: Painter,
) {
    var inputValue by inputValueState

    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                inputValue = it
            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth(fraction = 0.7f)
                .then(modifier),
            label = { Text(inputLabel + inputTip) },
            isError = inputTip.isNotEmpty(),
            placeholder = { Text(inputPlaceHolder) },
            keyboardOptions = keyboardOptions,
            maxLines = 1,

            leadingIcon = {
                ImageBox(
                    painter = icon
                )
            }
        )
    }
}

@Composable
fun ColumnScope.InputFieldWithMore(
    inputValueState: MutableState<String>,
    inputLabel: String,
    inputTip: String,
    inputPlaceHolder: String,
    modifier: Modifier = Modifier,
    inputEnabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Text
    ),
    icon: Painter,
    moreVisible: Boolean = false,
    dropDownMenu: @Composable() ColumnScope.(dismissCallBack: () -> Unit) -> Unit,
) {
    var inputValue by inputValueState
    var dropDownOpen by state(value = false)


    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                inputValue = it
            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth(fraction = 0.7f)
                .then(modifier),
            enabled = inputEnabled,
            label = { Text(inputLabel + inputTip) },
            isError = inputTip.isNotEmpty(),
            placeholder = { Text(inputPlaceHolder) },
            keyboardOptions = keyboardOptions,
            maxLines = 1,

            leadingIcon = {
                ImageBox(
                    painter = icon
                )
            },
            trailingIcon = {
                if (moreVisible) {
                    BoxWithConstraints(modifier = Modifier
                        .width(48.dp)
                        .padding(8.dp)
                        .clip(shape = CircleShape)
                        .clickable {
                            dropDownOpen = !dropDownOpen
                        }
                    ) {
                        Icon(painter = ImageResource.more(), contentDescription = null,
                            modifier = Modifier
                                .padding(8.dp),
                            tint = LocalPalette.current.primary
                        )
                    }

                    val callback = {
                        dropDownOpen = false
                    }

                    if (dropDownOpen) {
                        DropdownMenu(
                            modifier = Modifier.width(200.dp),
                            offset = DpOffset(x = 0.dp, y = 0.dp),
                            expanded = dropDownOpen,
                            onDismissRequest = { dropDownOpen = false }) {
                            dropDownMenu(callback)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ColumnScope.PasswordField(
    inputValueState: MutableState<String>,
    inputLabel: String,
    inputTip: String,
    inputPlaceHolder: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Password
    )
) {
    var inputValue by inputValueState

    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                inputValue = it
            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth(fraction = 0.7f)
                .then(modifier),
            label = { Text(inputLabel + inputTip) },
            isError = inputTip.isNotEmpty(),
            placeholder = { Text(inputPlaceHolder) },
            keyboardOptions = keyboardOptions,
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                ImageBox(
                    painter = icon
                )
            }
        )
    }
}

@Composable
fun ColumnScope.ButtonField(
    text: String,
    onActionText: String,
    isOnAction: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(8.dp)
            .fillMaxWidth(0.7f), onClick = { onClick() },
        enabled = isEnabled && !isOnAction,
    ) {
        if (isOnAction) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = DailySetTheme.color.primaryColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                onActionText,
                modifier = Modifier.padding(4.dp),
                style = DailySetTheme.typography.buttonText
            )
        } else {
            Text(
                text,
                modifier = Modifier.padding(4.dp),
                style = DailySetTheme.typography.buttonText
            )
        }


    }
}