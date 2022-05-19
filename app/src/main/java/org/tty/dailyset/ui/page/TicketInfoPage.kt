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
import androidx.compose.ui.unit.sp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.bean.entity.UserTicketInfo
import org.tty.dailyset.component.common.DialogVM
import org.tty.dailyset.component.ticket.info.TicketInfoVM
import org.tty.dailyset.component.ticket.info.rememberTicketInfoVM
import org.tty.dailyset.ui.component.NanoDialog
import org.tty.dailyset.ui.component.NanoDialogButton
import org.tty.dailyset.ui.component.ProfileMenuItem
import org.tty.dailyset.ui.component.TopBar
import org.tty.dailyset.ui.theme.DailySetTheme

@Composable
@UseViewModel("/ticket/info")
fun TicketInfoPage() {
    val nav = LocalNav.current
    val ticketInfoVM = rememberTicketInfoVM()
    val userTicketInfo by ticketInfoVM.userTicketInfo.collectAsState()
    val forceFetchDialogVM = ticketInfoVM.forceFetchDialogVM
    val unbindTicketDialogVM = ticketInfoVM.unbindTicketDialogVM

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
        TicketInfoButtons(
            forceFetchDialogVM = forceFetchDialogVM,
            unbindTicketDialogVM = unbindTicketDialogVM,
            ticketInfoVM = ticketInfoVM
        )
    }

    ForceFetchDialogCover(forceFetchDialogVM = forceFetchDialogVM, ticketInfoVM = ticketInfoVM)
    UnBindTicketDialogCover(unbindTicketDialogVM = unbindTicketDialogVM, ticketInfoVM = ticketInfoVM)
    
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

@Suppress("unused")
@Composable
fun ColumnScope.TicketInfoButtons(
    forceFetchDialogVM: DialogVM,
    unbindTicketDialogVM: DialogVM,
    ticketInfoVM: TicketInfoVM
) {
    val userTicketInfo by ticketInfoVM.userTicketInfo.collectAsState()

    OutlinedButton(
        onClick = { forceFetchDialogVM.dialogOpen.value = true },
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
        onClick = { unbindTicketDialogVM.dialogOpen.value = true },
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

@Composable
fun ForceFetchDialogCover(
    forceFetchDialogVM: DialogVM, ticketInfoVM: TicketInfoVM
) {
    NanoDialog(title = stringResource(R.string.ticket_force_fetch), dialogVM = forceFetchDialogVM) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.ticket_force_fetch_confirm)
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.ticket_force_fetch_confirm_tip), fontSize = 14.sp
        )
        NanoDialogButton(text = stringResource(R.string.confirm)) {
            ticketInfoVM.forceFetchTicket()
            forceFetchDialogVM.dialogOpen.value = false
        }
    }
}

@Composable
fun UnBindTicketDialogCover(
    unbindTicketDialogVM: DialogVM,
    ticketInfoVM: TicketInfoVM
) {
    NanoDialog(title = stringResource(R.string.ticket_unbind), dialogVM = unbindTicketDialogVM) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.ticket_unbind_confirm)
        )
        NanoDialogButton(text = stringResource(R.string.confirm), error = true) {
            ticketInfoVM.unbindTicket()
            unbindTicketDialogVM.dialogOpen.value = false
        }
    }
}