package org.tty.dailyset.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.tty.dailyset.bean.lifetime.DialogState
import org.tty.dailyset.component.common.DialogVM
import org.tty.dailyset.component.common.asMutableState
import org.tty.dailyset.ui.theme.DailySetTheme
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.ui.theme.Shapes

@Composable
fun NanoDialog(
    title: String,
    dialogState: DialogState,
    autoClose: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,

    ) {

    var dialogOpen by dialogState.dialogOpen
    val interactionSource1 = remember {
        MutableInteractionSource()
    }
    val interactionSource2 = remember {
        MutableInteractionSource()
    }

    /**
     * Use System Service: [android.view.inputmethod.InputMethodManager]
     */

    fun closeDialog() {
        if (autoClose) {
            dialogOpen = false
        }
    }

    if (dialogOpen) {
        Dialog(
            onDismissRequest = {
                closeDialog()
            },
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(interactionSource = interactionSource1, indication = null) {
                        closeDialog()
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = Shapes.medium)
                        .background(color = MaterialTheme.colors.background)
                        .align(alignment = Alignment.Center)
                        .padding(16.dp)

                        .clickable(interactionSource = interactionSource2, indication = null) {}
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalPalette.current.primaryColor,
                        text = title
                    )
                    content()
                }
            }

            /**
             * intercept the back event to hide the dialog.
             */
            BackHandler {
                closeDialog()
            }
        }
    }
}

@Composable
fun NanoDialog(
    title: String,
    dialogVM: DialogVM,
    autoClose: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    NanoDialog(
        title = title,
        dialogState = DialogState(dialogOpen = dialogVM.dialogOpen.asMutableState()),
        autoClose = autoClose,
        content = content
    )
}

@Composable
fun NanoDialogButton(
    text: String,
    error: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {

    val colors = if (error) {
        ButtonDefaults.outlinedButtonColors(
            contentColor = DailySetTheme.color.error
        )
    } else {
        ButtonDefaults.outlinedButtonColors(
            contentColor = DailySetTheme.color.primaryColor
        )
    }

    Row(
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(
            colors = colors,
            enabled = enabled,
            onClick = onClick
        ) {
            Text(text)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomDialog(
    dialogState: DialogState,
    autoClose: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    var dialogOpen by dialogState.dialogOpen
    val interactionSource1 = remember { MutableInteractionSource() }
    val interactionSource2 = remember { MutableInteractionSource() }

    fun closeDialog() {
        if (autoClose) {
            dialogOpen = false
        }
    }

    if (dialogOpen) {
        Dialog(
            onDismissRequest = { closeDialog() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(interactionSource = interactionSource1, indication = null) {
                        closeDialog()
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = Shapes.medium)
                        .background(color = MaterialTheme.colors.background)
                        .align(alignment = Alignment.BottomCenter)
                        .padding(16.dp)
                        .clickable(interactionSource = interactionSource2, indication = null) {}
                ) {
                    content()
                }
            }

            /**
             * intercept the back event to hide the dialog.
             */
            BackHandler {
                closeDialog()
            }
        }
    }
}