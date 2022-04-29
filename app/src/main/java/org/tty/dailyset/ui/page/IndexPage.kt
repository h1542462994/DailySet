package org.tty.dailyset.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.R
import org.tty.dailyset.component.common.StatusBarToBackground
import org.tty.dailyset.ui.image.ImageResource

@Composable
fun IndexPage() {
    StatusBarToBackground()

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.weight(1.0f))
        Row(modifier = Modifier.weight(6.0f)) {
            Spacer(modifier = Modifier.weight(1.0f))
            Image(painter = ImageResource.dailySet(), contentDescription = "", modifier = Modifier
                .align(
                    Alignment.CenterVertically
                )
                .weight(0.7f))
            Spacer(modifier = Modifier.weight(1.0f))
        }
        Box(modifier = Modifier.weight(4.0f)) {
            Box(modifier = Modifier.align(Alignment.Center)) {
                Text(text = stringResource(id = R.string.app_name), modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.h5, textAlign = TextAlign.Center)
            }
        }
    }


}

@Preview
@Composable
fun IndexPagePreview() {
    IndexPage()
}