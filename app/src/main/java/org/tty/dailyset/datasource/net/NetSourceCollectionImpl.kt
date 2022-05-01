package org.tty.dailyset.datasource.net

import org.tty.dailyset.bean.enums.PreferenceName
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.observeOnApplicationScope
import org.tty.dailyset.datasource.NetSourceCollection

class NetSourceCollectionImpl(private val sharedComponents: SharedComponents): NetSourceCollection {

    private val retrofitFactory = RetrofitFactory()
    private var address = PreferenceName.CURRENT_HTTP_SERVER_ADDRESS.defaultValue
    private var userService0: UserService = retrofitFactory.defaultRetrofit().create(UserService::class.java)

    override val userService: UserService
        get() = userService0

    override suspend fun init() {
        sharedComponents.stateStore.currentHttpServerAddress.observeOnApplicationScope {
            createWithAddress(it)
        }
        val address: String = sharedComponents.repositoryCollection.preferenceRepository.read(PreferenceName.CURRENT_HTTP_SERVER_ADDRESS)
        createWithAddress(address)
    }

    private suspend fun createWithAddress(address: String) {
        if (address != this.address) {
            this.address = address
            val result: String = sharedComponents.repositoryCollection.preferenceRepository.read(PreferenceName.CURRENT_HTTP_SERVER_ADDRESS)
            val retrofit = retrofitFactory.normalRetrofit(result)
            userService0 = retrofit.create(UserService::class.java)
        }
    }
}