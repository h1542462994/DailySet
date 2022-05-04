package org.tty.dailyset.converter

import org.tty.dailyset.bean.enums.UnicTickStatus
import org.tty.dailyset.dailyset_cloud.grpc.TicketBindInfo
import org.tty.dailyset.dailyset_cloud.grpc.TicketBindInfo.TicketStatus

fun TicketBindInfo.TicketStatus.toUnicTicketStatus(): UnicTickStatus {
    return when(this) {
        TicketStatus.Initialized -> UnicTickStatus.Initialized
        TicketStatus.Checked -> UnicTickStatus.Checked
        TicketStatus.Failure -> UnicTickStatus.UnknownFailure
        TicketStatus.PasswordFailure -> UnicTickStatus.PasswordFailure
        else -> UnicTickStatus.NotBind
    }
}