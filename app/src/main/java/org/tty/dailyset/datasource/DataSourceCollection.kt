package org.tty.dailyset.datasource

import org.tty.dailyset.datasource.runtime.RuntimeDataSource

interface DataSourceCollection {
    val dbSourceCollection: DbSourceCollection
    val netSourceCollection: NetSourceCollection
    val runtimeDataSource: RuntimeDataSource
    val grpcSourceCollection: GrpcSourceCollection
}