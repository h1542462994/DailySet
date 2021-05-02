package org.tty.dailyset.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.model.lifetime.DialogState
import org.tty.dailyset.ui.theme.LocalPalette

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
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(color = LocalPalette.current.backgroundTransparent)
                .clickable(interactionSource = interactionSource1, indication = null) {
                    dialogOpen = false
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.background)
                    .align(alignment = Alignment.BottomCenter)
                    .padding(16.dp)
                    .clickable(interactionSource = interactionSource2, indication = null) {}
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
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

