package org.tty.dailyset.datasource.net

import okhttp3.Interceptor
import okhttp3.Response

object TokenInterceptor: Interceptor {
    var token: String = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (token.isNotEmpty()) {
            request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }

        val response = chain.proceed(request)
        val newToken = response.header("Authorization")
        if (!newToken.isNullOrEmpty()) {
            this.token = newToken.substring("Bearer ".length)
        }
        return response
    }
}