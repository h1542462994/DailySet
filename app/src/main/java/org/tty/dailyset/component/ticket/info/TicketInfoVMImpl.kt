package org.tty.dailyset.component.ticket.info

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tty.dailyset.MainDestination
import org.tty.dailyset.bean.entity.EntityDefaults
import org.tty.dailyset.bean.entity.UserTicketInfo
import org.tty.dailyset.component.common.*
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
        sharedComponents.stateStore.userTicketInfo.asAppStateFlow(EntityDefaults.emptyUserTicketInfo())

    override val forceFetchDialogVM: DialogVM = SimpleDialogVMImpl(false)

    override val unbindTicketDialogVM: DialogVM = SimpleDialogVMImpl(false)

    override fun forceFetchTicket() {
        sharedComponents.activityScope.launch {
            sharedComponents.actorCollection.userActor.forceFetchTicket()
        }
    }

    override fun rebindTicket(userTicketInfo: UserTicketInfo) {
        val nav = sharedComponents.nav
        nav.action.toTicketBind(
            TicketBindInput(from = MainDestination.TICKET_INFO_ROUTE, studentUid = userTicketInfo.shortStudentUid)
        )
    }

    override fun unbindTicket() {
        sharedComponents.activityScope.launch {
            sharedComponents.actorCollection.userActor.unbindTicket()
        }
    }
}