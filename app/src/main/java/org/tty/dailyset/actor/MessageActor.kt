package org.tty.dailyset.actor

import kotlinx.coroutines.*
import org.tty.dailyset.bean.enums.MessageTopics
import org.tty.dailyset.bean.enums.PreferenceName
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.dailyset_cloud.grpc.MessageBindRequest
import org.tty.dailyset.dailyset_cloud.grpc.Token
import org.tty.dailyset.component.common.BaseVM
import org.tty.dailyset.datasource.DataSourceCollection

/**
 * **actor** for **MessageBundle**,
 * interaction between [BaseVM] and [DataSourceCollection]
 * @see [ActorCollection]
 */
class MessageActor(private val sharedComponents: SharedComponents) {
    private var job: Job? = null

    /**
     * connect the message service.
     */
    fun startConnect() {
        synchronized(this) {
            if (job == null) {
                job = sharedComponents.applicationScope.launch {
                    withContext(Dispatchers.IO) {
                        var retryCount = 0
                        while (true) {
                            doConnect()
                            retryCount += 1
                            logger.d("MessageActor", "连接消息服务失败，正在进行第${retryCount}次重试")
                            delay(10000)
                            if (retryCount > 10) {
                                break
                            }
                        }
                    }
                }
                job?.invokeOnCompletion { job = null }
            } else {
                job?.cancel()
                job = null
                startConnect()
            }
        }
    }

    fun endConnect() {
        synchronized(this) {
            job?.cancel()
            job = null
        }
    }


    private suspend fun doConnect() {
        val currentUserUid: String = sharedComponents.actorCollection.preferenceActor.read(PreferenceName.CURRENT_USER_UID)
        val user = sharedComponents.database.userDao().get(currentUserUid) ?: return
        try {
            val receivedChannel = sharedComponents.dataSourceCollection.grpcSourceCollection.messageService()
                .connect(request = MessageBindRequest.newBuilder().setToken(Token.newBuilder().setValue(user.token)).build())

            logger.d("MessageActor", "正在连接服务器.")

            while (true) {
                // the coroutine will be suspend until receive a new message
                val bundle = receivedChannel.receive()
                logger.d("MessageActor", "收到了服务器的消息: ${bundle.topic}, ${bundle.referer}, ${bundle.code}, ${bundle.content}")

                if (bundle.topic == MessageTopics.dailySetUnicTicket) {
                    sharedComponents.actorCollection.userActor.updateCurrentBindInfo()
                } else if (bundle.topic == MessageTopics.dailySetUnicCourse) {
                    sharedComponents.actorCollection.dailySetActor.updateData()
                }
            }
        } catch (e: Exception) {
            logger.e("MessageActor", "连接消息服务失败: $e")
        }
    }
}