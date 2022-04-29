package org.tty.dailyset.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import org.tty.dailyset.ui.theme.LocalPalette

@Composable
fun ImageBox(painter: Painter, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = Modifier
        .width(56.dp)
        .padding(8.dp)
        .clip(shape = CircleShape)
        .then(modifier)
    ) {
        Icon(painter = painter, contentDescription = null,
            modifier = Modifier
                .padding(8.dp),
            tint = LocalPalette.current.primary
        )
    }
}