package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.tty.dailyset.LocalNav
import org.tty.dailyset.MainDestination
import org.tty.dailyset.R
import org.tty.dailyset.annotation.UseViewModel
import org.tty.dailyset.component.common.StatusBarToBackground
import org.tty.dailyset.component.common.asMutableState
import org.tty.dailyset.component.ticket.bind.TicketBindInput
import org.tty.dailyset.component.ticket.bind.rememberTicketBindVM
import org.tty.dailyset.ui.component.ButtonField
import org.tty.dailyset.ui.component.CenterBar
import org.tty.dailyset.ui.component.InputField
import org.tty.dailyset.ui.component.PasswordField
import org.tty.dailyset.ui.image.ImageResource

@UseViewModel("/ticket/bind")
@Composable
fun TicketBindPage(ticketBindInput: TicketBindInput) {
    StatusBarToBackground()

    val ticketBindVM = rememberTicketBindVM()

    val studentUidTextState = ticketBindVM.studentUidText.asMutableState()
    val passwordTextState = ticketBindVM.passwordText.asMutableState()
    val studentTipValue by ticketBindVM.studentUidTipValue.collectAsState()
    val passwordTipValue by ticketBindVM.passwordTipValue.collectAsState()
    val ticketBindButtonEnabled by ticketBindVM.ticketBindButtonEnabled.collectAsState()
    val isOnTicketBind by ticketBindVM.isOnTicketBind.collectAsState()


    val nav = LocalNav.current
    val isRebind = ticketBindInput.from == MainDestination.TICKET_INFO_ROUTE
    val title = if (isRebind) {
        stringResource(id = R.string.ticket_bind_title_rebind)
    } else {
        stringResource(id = R.string.ticket_bind_title)
    }
    val buttonText = if (isRebind) {
        stringResource(id = R.string.ticket_rebind)
    } else {
        stringResource(id = R.string.ticket_bind)
    }

    Column {
        CenterBar(
            useBack = true,
            content = title,
            onBackPressed = {
                nav.action.upPress()
            }
        )

        Spacer(modifier = Modifier.height(128.dp))
        InputField(
            inputValueState = studentUidTextState,
            inputLabel = stringResource(id = R.string.ticket_student_uid_field),
            inputTip = studentTipValue,
            inputPlaceHolder = stringResource(id = R.string.ticket_student_uid_field_tip),
            icon = ImageResource.nickname(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        Spacer(modifier = Modifier.height(8.dp))
        PasswordField(
            inputValueState = passwordTextState,
            inputLabel = stringResource(id = R.string.ticket_student_password_field),
            inputTip = passwordTipValue,
            inputPlaceHolder = stringResource(id = R.string.ticket_student_password_field_tip),
            icon = ImageResource.password()
        )

        Spacer(modifier = Modifier.height(8.dp))

        ButtonField(
            text = buttonText,
            onActionText = stringResource(id = R.string.ticket_bind_on_bind),
            isOnAction = isOnTicketBind,
            isEnabled = ticketBindButtonEnabled,
        ) {
            if (isRebind) {
                ticketBindVM.rebindTicket(nav.action)
            } else {
                ticketBindVM.bindTicket(nav.action)
            }
        }

        Spacer(modifier = Modifier.weight(1.0f))
    }
}