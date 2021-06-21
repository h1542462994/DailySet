package org.tty.dailyset.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import org.tty.dailyset.model.lifetime.DialogState
import org.tty.dailyset.ui.theme.LocalPalette
import org.tty.dailyset.ui.theme.Shapes

@Composable
fun NanoDialog(
    title: String,
    dialogState: DialogState,
    content: @Composable ColumnScope.() -> Unit
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


    if (dialogOpen) {
        Dialog(
            onDismissRequest = { dialogOpen = false },
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(interactionSource = interactionSource1, indication = null) {
                        dialogOpen = false
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
                dialogOpen = false
            }
        }
    }
}

@Composable
fun NanoDialogButton(text: String, error: Boolean = false, enabled: Boolean = true, onClick: () -> Unit) {

    val colors = if (error) {
        ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.error,
            disabledBackgroundColor = LocalPalette.current.backgroundInvalid,
            disabledContentColor = LocalPalette.current.primary
        )
    } else {
        ButtonDefaults.buttonColors(
            disabledBackgroundColor = LocalPalette.current.backgroundInvalid,
            disabledContentColor = LocalPalette.current.primary
        )
    }

    Row(
        modifier = Modifier.padding(vertical = 16.dp),
    ){
        Spacer(modifier = Modifier.weight(1f))
        Button(
            colors = colors,
            enabled = enabled,
            onClick = onClick) {
            Text(text)
        }
    }
}

