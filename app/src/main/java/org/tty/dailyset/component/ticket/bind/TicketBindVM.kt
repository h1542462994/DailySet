package org.tty.dailyset.component.ticket.bind

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.MainActions

interface TicketBindVM {
    val ticketBindButtonEnabled: StateFlow<Boolean>
    val studentUidText: MutableStateFlow<String>
    val passwordText: MutableStateFlow<String>
    val studentUidTipValue: StateFlow<String>
    val passwordTipValue: StateFlow<String>
    val isOnTicketBind: StateFlow<Boolean>
    fun bindTicket(navAction: MainActions)
    fun rebindTicket(navAction: MainActions)
}