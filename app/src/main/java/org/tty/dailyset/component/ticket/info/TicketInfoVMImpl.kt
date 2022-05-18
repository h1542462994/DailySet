package org.tty.dailyset.component.ticket.info

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.MainDestination
import org.tty.dailyset.bean.entity.DefaultEntities
import org.tty.dailyset.bean.entity.UserTicketInfo
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.asActivityColdStateFlow
import org.tty.dailyset.component.common.sharedComponents
import org.tty.dailyset.component.ticket.bind.TicketBindInput

@Composable
fun rememberTicketInfoVM(): TicketInfoVM {
    val sharedComponents = sharedComponents()
    return sharedComponents.viewModelStore.getVM("ticketInfoVM") {
        TicketInfoVMImpl(sharedComponents)
    }
}

internal class TicketInfoVMImpl(private val sharedComponents: SharedComponents) : TicketInfoVM {
    override val userTicketInfo: StateFlow<UserTicketInfo> =
        sharedComponents.stateStore.userTicketInfo.asActivityColdStateFlow(DefaultEntities.emptyUserTicketInfo())

    override fun forceFetchTicket() {
        //
    }

    override fun rebindTicket(userTicketInfo: UserTicketInfo) {
        val nav = sharedComponents.nav
        nav.action.toTicketBind(
            TicketBindInput(from = MainDestination.TICKET_INFO_ROUTE, studentUid = userTicketInfo.shortStudentUid)
        )
    }

    override fun unbindTicket() {
        //
    }
}