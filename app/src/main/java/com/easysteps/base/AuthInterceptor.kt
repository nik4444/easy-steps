package com.easysteps.base

import com.easysteps.api.IBaseService
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .method(original.method, original.body)
            .header(IBaseService.Accept, "application/json")
        return chain.proceed(requestBuilder.build())
    }
}