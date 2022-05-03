@file:OptIn(ExperimentalSerializationApi::class)

package org.tty.dailyset.datasource.net

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.tty.dailyset.bean.enums.PreferenceName
import org.tty.dailyset.component.common.observeOnApplicationScope
import org.tty.dailyset.component.common.sharedComponents
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class RetrofitFactory {

    fun defaultRetrofit(): Retrofit {
        return normalRetrofit(PreferenceName.CURRENT_HTTP_SERVER_ADDRESS.defaultValue)
    }

    fun normalRetrofit(address: String): Retrofit {
        val contentType = "application/json".toMediaType()
//        val json = Json {
//            explicitNulls = true
//        }
        return Retrofit.Builder()
            .client(okHttpClient())
            .baseUrl(address)
            .addConverterFactory(Json.asConverterFactory(contentType))
//            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
    }



}