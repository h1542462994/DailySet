package org.tty.dailyset.ui.page

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.entity.User
import org.tty.dailyset.model.lifetime.UserState

@Preview
@Composable
fun DailyTableTitlePreview() {
    DailyTableTitle(
        dailyTable = DailyTable.default(),
        userState = UserState(
            currentUser = User.default(),
            User.local
        )
    )
}