package org.tty.dailyset.ui.page

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.tty.dailyset.R
import org.tty.dailyset.data.processor.DailySetProcessor
import org.tty.dailyset.model.lifetime.dailyset.DailySetCreateState
import org.tty.dailyset.ui.component.NanoDialog

@Composable
fun DailySetCreateDialogCover(
    dailySetProcessor: DailySetProcessor,
    dailySetCreateState: DailySetCreateState
) {
    NanoDialog(title = stringResource(R.string.add_list), dialogState = dailySetCreateState) {
        Text("hello world")
    }
}