package org.tty.dailyset.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.DismissState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tty.dailyset.model.lifetime.DialogState
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
    }
}

