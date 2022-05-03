package org.tty.dailyset.datasource.grpc

import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.dailyset_cloud.grpc.HelloCoroutineGrpc
import org.tty.dailyset.dailyset_cloud.grpc.TicketServiceCoroutineGrpc
import org.tty.dailyset.datasource.GrpcSourceCollection

class GrpcSourceCollectionImpl(private val sharedComponents: SharedComponents): GrpcSourceCollection {

    private var grpcClientFactory: GrpcClientFactory? = null

    private var ticketService0: TicketServiceCoroutineGrpc.TicketServiceCoroutineStub? = null

    private var helloService0: HelloCoroutineGrpc.HelloCoroutineStub? = null

    override suspend fun ticketService(): TicketServiceCoroutineGrpc.TicketServiceCoroutineStub {
        requireNotNull(grpcClientFactory) { "grpcClientFactory is null" }
//        if (ticketService0 == null) {
//            ticketService0 = TicketServiceCoroutineGrpc.newStub(grpcClientFactory!!.getChannel())
//        }
//        return ticketService0!!
        return TicketServiceCoroutineGrpc.newStub(grpcClientFactory!!.getChannel())
    }

    override suspend fun helloService(): HelloCoroutineGrpc.HelloCoroutineStub {
        requireNotNull(grpcClientFactory) { "grpcClientFactory is null" }
//        if (helloService0 == null) {
//            helloService0 = HelloCoroutineGrpc.newStub(grpcClientFactory!!.getChannel())
//        }
//        return helloService0!!
        return HelloCoroutineGrpc.newStub(grpcClientFactory!!.getChannel())
    }

    override suspend fun init() {
        grpcClientFactory = GrpcClientFactory(sharedComponents)
    }
}