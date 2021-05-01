package org.tty.dailyset.ui.component

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tty.dailyset.DailySetApplication
import org.tty.dailyset.model.lifetime.DialogState
import org.tty.dailyset.provider.LocalServices
import org.tty.dailyset.ui.theme.LocalPalette

@Composable
fun NanoDialog(
    dialogState: DialogState,
    content: @Composable ColumnScope.() -> Unit
    ) {

    var dialogOpen by dialogState.dialogOpen
    val interactionSource = remember {
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
                .clickable(interactionSource = interactionSource, indication = null) {
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

