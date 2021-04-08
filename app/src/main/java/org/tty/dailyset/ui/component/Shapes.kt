package org.tty.dailyset.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun Badge(
    borderColor: Color,
    background: Color,
    textColor: Color,
    text: String,
    onClick: (() -> Unit)? = null
) {
    var modifier = Modifier
        .background(background)

    if (onClick != null){
        modifier = modifier.clickable { onClick() }
    }

    modifier = modifier.border(2.dp, color = borderColor, shape = RectangleShape)
        .padding(horizontal = 8.dp, vertical = 2.dp)

    Text(
        modifier = modifier,
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = textColor
    )
}

@Composable
fun Badge(
    readOnly: Boolean,
    checked: Boolean,
    text: String,
    onClick: () -> Unit
) {
    if (checked && !readOnly) {
        Badge(borderColor = LocalPalette.current.backgroundColor, background = MaterialTheme.colors.primary, textColor = MaterialTheme.colors.surface, text = text, onClick = onClick)
    } else if (checked && readOnly) {
        Badge(borderColor = LocalPalette.current.backgroundColor, background = LocalPalette.current.background3, textColor = MaterialTheme.colors.surface, text = text)
    } else if (!checked && !readOnly) {
        Badge(borderColor = LocalPalette.current.background1, background = MaterialTheme.colors.background, textColor = MaterialTheme.colors.onSurface, text = text, onClick = onClick)
    } else if (!checked && readOnly) {
        Badge(borderColor = LocalPalette.current.background1, background = LocalPalette.current.background1, textColor = MaterialTheme.colors.onSurface, text = text)
    }
}
