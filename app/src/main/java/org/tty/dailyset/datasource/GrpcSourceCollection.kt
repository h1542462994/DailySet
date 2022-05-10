package org.tty.dailyset.datasource

import org.tty.dailyset.component.common.SuspendInit
import org.tty.dailyset.dailyset_cloud.grpc.HelloCoroutineGrpc
import org.tty.dailyset.dailyset_cloud.grpc.MessageServiceCoroutineGrpc
import org.tty.dailyset.dailyset_cloud.grpc.TicketServiceCoroutineGrpc


interface GrpcSourceCollection: SuspendInit {
    suspend fun ticketService(): TicketServiceCoroutineGrpc.TicketServiceCoroutineStub
    suspend fun helloService(): HelloCoroutineGrpc.HelloCoroutineStub
    suspend fun messageService(): MessageServiceCoroutineGrpc.MessageServiceCoroutineStub
}