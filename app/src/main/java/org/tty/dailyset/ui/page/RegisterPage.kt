package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.tty.dailyset.ui.component.CenterBar

@Composable
fun RegisterPage() {
    Column {
        CenterBar(content = "注册")
        Spacer(modifier = Modifier.weight(1.0f))

    }
}