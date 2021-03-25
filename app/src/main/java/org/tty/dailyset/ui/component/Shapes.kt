package org.tty.dailyset.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.tty.dailyset.ui.theme.LocalPalette

@Composable
fun Oval(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawOval(color)
    }
}

@Composable
fun TitleSpace(title: String) {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
    ) {
        val spacer = @Composable {
            Spacer(
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(8.dp)
                    .height(1.dp)
                    .weight(1f)
                    .background(color = LocalPalette.current.background2)
            )
        }
        spacer()
        Text(text = title,
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            color = LocalPalette.current.textColorDetail
        )
        spacer()
    }
}

