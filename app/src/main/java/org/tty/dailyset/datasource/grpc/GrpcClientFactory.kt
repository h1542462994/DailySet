package org.tty.dailyset.datasource.grpc

import android.annotation.SuppressLint
import com.squareup.okhttp.ConnectionSpec
import io.grpc.ManagedChannel
import io.grpc.okhttp.OkHttpChannelBuilder
import org.tty.dailyset.bean.enums.PreferenceName
import org.tty.dailyset.component.common.SharedComponents
import java.security.cert.X509Certificate
import javax.net.ssl.*

class GrpcClientFactory(private val sharedComponents: SharedComponents) {

    private var channel: ManagedChannel? = null

    @SuppressLint("TrustAllX509TrustManager", "CustomX509TrustManager")
    fun getSocketFactory(): SSLSocketFactory {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<X509Certificate>, authType: String
            ) {
            }

            override fun checkServerTrusted(
                chain: Array<X509Certificate>, authType: String
            ) {

            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })
        val sc = SSLContext.getInstance("TLS")
        sc.init(null, trustAllCerts, java.security.SecureRandom())
        return sc.socketFactory
    }

    suspend fun getChannel(): ManagedChannel {
        val address: String =
            sharedComponents.actorCollection.preferenceActor.read(PreferenceName.CURRENT_HOST)

        if (channel == null) {
            channel = OkHttpChannelBuilder.forAddress(address, 8087)
                .connectionSpec(ConnectionSpec.MODERN_TLS).sslSocketFactory(getSocketFactory())
                .hostnameVerifier { _, _ -> true }
                .build()
        }

        return channel!!
    }


}