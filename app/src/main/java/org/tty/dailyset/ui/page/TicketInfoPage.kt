package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.bean.entity.UserTicketInfo
import org.tty.dailyset.component.ticket.info.TicketInfoVM
import org.tty.dailyset.component.ticket.info.rememberTicketInfoVM
import org.tty.dailyset.ui.component.ProfileMenuItem
import org.tty.dailyset.ui.component.TopBar
import org.tty.dailyset.ui.theme.DailySetTheme

@Composable
@UseViewModel("/ticket/info")
fun TicketInfoPage() {
    val nav = LocalNav.current
    val ticketInfoVM = rememberTicketInfoVM()
    val userTicketInfo by ticketInfoVM.userTicketInfo.collectAsState()
    Column {
        TopBar(title = stringResource(id = R.string.ticket_info_title),
            useBack = true,
            onBackPressed = {
                nav.action.upPress()
            })
        LazyColumn(modifier = Modifier.weight(1.0f)) {
            item {
                TicketInfoCurrent(userTicketInfo = userTicketInfo)
            }
        }
        TicketInfoButtons(ticketInfoVM = ticketInfoVM)
    }
}

@Composable
fun TicketInfoCurrent(
    userTicketInfo: UserTicketInfo
) {
    Column {
        ProfileMenuItem(
            title = stringResource(id = R.string.ticket_info_student_uid),
            content = userTicketInfo.shortStudentUid
        )
        ProfileMenuItem(
            title = stringResource(id = R.string.ticket_info_department_name),
            content = userTicketInfo.departmentName
        )
        ProfileMenuItem(
            title = stringResource(id = R.string.ticket_info_clazz_name),
            content = userTicketInfo.clazzName
        )
        ProfileMenuItem(
            title = stringResource(id = R.string.ticket_info_student_name),
            content = userTicketInfo.name
        )
        ProfileMenuItem(
            title = stringResource(id = R.string.ticket_info_student_name),
            content = userTicketInfo.grade.toString()
        )
    }
}

@Composable
fun ColumnScope.TicketInfoButtons(
    ticketInfoVM: TicketInfoVM
) {
    val userTicketInfo by ticketInfoVM.userTicketInfo.collectAsState()

    OutlinedButton(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.ticket_force_fetch),
            style = DailySetTheme.typography.buttonText
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedButton(
        onClick = { ticketInfoVM.rebindTicket(userTicketInfo) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.ticket_rebind),
            style = DailySetTheme.typography.buttonText
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedButton(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = DailySetTheme.color.error),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.ticket_unbind),
            style = DailySetTheme.typography.buttonText
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
}