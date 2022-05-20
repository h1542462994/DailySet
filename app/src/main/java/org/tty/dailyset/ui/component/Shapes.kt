package org.tty.dailyset.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
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
                    .background(color = LocalPalette.current.lineColor)
            )
        }
        spacer()
        Text(text = title,
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            color = LocalPalette.current.sub
        )
        spacer()
    }
}

@Composable
fun TitleSpace() {
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
                    .background(color = LocalPalette.current.lineColor)
            )
        }
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

    modifier = modifier
        .border(2.dp, color = borderColor, shape = RectangleShape)
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
        Badge(borderColor = LocalPalette.current.backgroundColor, background = LocalPalette.current.primaryColor, textColor = LocalPalette.current.primarySurface, text = text, onClick = onClick)
    } else if (checked && readOnly) {
        Badge(borderColor = LocalPalette.current.backgroundColor, background = LocalPalette.current.backgroundColor, textColor = LocalPalette.current.textColorInValid, text = text)
    } else if (!checked && !readOnly) {
        Badge(borderColor = LocalPalette.current.backgroundColor, background = LocalPalette.current.primarySurface, textColor = LocalPalette.current.primary, text = text, onClick = onClick)
    } else if (!checked && readOnly) {
        Badge(borderColor = LocalPalette.current.backgroundInvalid, background = LocalPalette.current.backgroundInvalid, textColor = LocalPalette.current.textColorInValid, text = text)
    }
}

@Composable
fun RoundBadge(
    checked: Boolean,
    text: String,
    onClick: (() -> Unit)? = null
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    var modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)

    modifier = if (checked) {
        modifier
            .background(color = LocalPalette.current.primaryColor)
    } else {
        modifier
            .border(width = 2.dp, color = LocalPalette.current.primaryColor)
    }

    if (onClick != null) {
        modifier = modifier.clickable(
            interactionSource = interactionSource, indication = null
        ) {
            onClick()
        }
    }
    modifier = modifier.padding(horizontal = 12.dp, vertical = 6.dp)

    BoxWithConstraints(
        modifier = modifier
    ) {
        Text(
            text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (checked){
                LocalPalette.current.primarySurface
            } else {
                LocalPalette.current.primary
            }

        )
    }
}

@Composable
fun IconClick(
    painter: Painter,
    modifier: Modifier = Modifier,
    useTint: Boolean = false,
    onClick: () -> Unit

) {
    BoxWithConstraints(
        modifier = Modifier
            .size(56.dp)
            .wrapContentSize(align = Alignment.Center)
            .clip(shape = CircleShape)
            .clickable { onClick() }
            .then(modifier)
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .padding(12.dp)
                .align(alignment = Alignment.Center),
            tint = if (useTint) { LocalPalette.current.primary } else { Color.Unspecified }
        )
    }
}