package org.tty.dailyset.ui.page

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.model.lifetime.dailyset.DailySetCreateState

@Preview
@Composable
fun DailySetAddPartPreview() {
    with(DataScope) {
        DailySetAddPart(dailySetCreateState = dailySetCreateState())
    }
}

@Preview
@Composable
fun DailySetAutoPartPreview() {
    DailySetAutoPart()
}

@Preview
@Composable
fun DailySetUserPartPreview() {
    DailySetUserPart()
}