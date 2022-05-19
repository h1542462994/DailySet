package org.tty.dailyset.component.ticket.info

import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.entity.UserTicketInfo
import org.tty.dailyset.component.common.DialogVM

interface TicketInfoVM {
    val userTicketInfo: StateFlow<UserTicketInfo>
    val forceFetchDialogVM: DialogVM
    val unbindTicketDialogVM: DialogVM

    fun forceFetchTicket()
    fun rebindTicket(userTicketInfo: UserTicketInfo)
    fun unbindTicket()
}