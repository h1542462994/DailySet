package org.tty.dailyset.bean.lifetime

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.tty.dailyset.R
import org.tty.dailyset.bean.entity.UserTicketInfo
import org.tty.dailyset.bean.enums.UnicTickStatus
import org.tty.dailyset.ui.theme.DailySetTheme

internal data class TicketStatusShow(
    val userTicketInfo: UserTicketInfo
) {
    val pointColor
        @Composable get() = when (userTicketInfo.status) {
            UnicTickStatus.NotBind -> DailySetTheme.color.statusGray
            UnicTickStatus.Initialized -> DailySetTheme.color.statusGray
            UnicTickStatus.Checked -> DailySetTheme.color.statusGreen
            UnicTickStatus.UnknownFailure -> DailySetTheme.color.statusOrange
            UnicTickStatus.PasswordFailure -> DailySetTheme.color.statusRed
        }


    val describeString
        @Composable get() = when (userTicketInfo.status) {
            UnicTickStatus.NotBind -> stringResource(id = R.string.ticket_status_not_bind)
            UnicTickStatus.Initialized -> stringResource(id = R.string.ticket_status_initializing)
            UnicTickStatus.Checked -> stringResource(id = R.string.ticket_status_normal)
            UnicTickStatus.UnknownFailure -> stringResource(id = R.string.unknown_error)
            UnicTickStatus.PasswordFailure -> stringResource(id = R.string.password_error)
        }
}

