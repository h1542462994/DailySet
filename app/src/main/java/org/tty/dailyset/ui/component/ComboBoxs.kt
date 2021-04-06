package org.tty.dailyset.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ComboBox(
    title: String,
    content: @Composable () -> Unit,
    popup: @Composable () -> Unit
) {
    var dropDownOpen by remember { mutableStateOf(false) }
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { dropDownOpen = true }
    ) {
        Row {
            Text(title)
            content()
        }
    }
    DropdownMenu(expanded = dropDownOpen, onDismissRequest = { dropDownOpen = false }) {
        popup()
    }
}

@Composable
fun <T> ComboBox(
    title: String,
    data: List<T>,
    defaultValue: T = data[0],
    onSelected: (T) -> Unit,
    menuTemplate: @Composable (T) -> Unit
){
    var dropDownOpen by remember { mutableStateOf(false) }
    var currentValue by remember {
        mutableStateOf(defaultValue)
    }

    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { dropDownOpen = true }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                title,
                modifier = Modifier
                    .wrapContentWidth()
                    .align(alignment = Alignment.CenterVertically),
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .wrapContentWidth()
            ) {
                menuTemplate(currentValue)
            }
        }
    }

    DropdownMenu(expanded = dropDownOpen, onDismissRequest = { dropDownOpen = false }) {
        data.forEach {
            DropdownMenuItem(onClick = { currentValue = it; onSelected(it) }) {
                menuTemplate(it)
            }
        }
    }
}