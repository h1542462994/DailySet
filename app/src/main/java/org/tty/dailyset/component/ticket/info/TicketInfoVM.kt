package org.tty.dailyset.component.ticket.info

import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.entity.UserTicketInfo

interface TicketInfoVM {
    val userTicketInfo: StateFlow<UserTicketInfo>

    fun forceFetchTicket()
    fun rebindTicket(userTicketInfo: UserTicketInfo)
    fun unbindTicket()
}