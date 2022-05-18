package org.tty.dailyset.bean.lifetime

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.tty.dailyset.R
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.bean.enums.PlatformState
import org.tty.dailyset.ui.theme.DailySetTheme

internal data class UserStatusShow(
    val user: User
) {
    val pointColor
        @Composable get() = when (user.state) {
            PlatformState.ALIVE.state -> DailySetTheme.color.statusGreen
            PlatformState.LEAVE.state -> DailySetTheme.color.statusGray
            PlatformState.INVALID.state -> DailySetTheme.color.statusOrange
            PlatformState.BAN.state -> DailySetTheme.color.statusRed
            else -> DailySetTheme.color.statusRed
        }

    val describeString
        @Composable get() = when (user.state) {
            PlatformState.ALIVE.state -> stringResource(id = R.string.user_state_alive)
            PlatformState.LEAVE.state -> stringResource(id = R.string.user_state_leave)
            PlatformState.INVALID.state -> stringResource(id = R.string.user_state_invalid)
            PlatformState.BAN.state -> stringResource(id = R.string.user_state_ban)
            else -> stringResource(R.string.unknown_error)
        }
}
