package com.easysteps.base

import com.easysteps.api.IBaseService
import com.easysteps.api.IBaseService.Companion.Authorization
import com.easysteps.pref.SharedPref
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .method(original.method, original.body)
            .header("Content-Type", "application/json")
            .header(IBaseService.Accept, "application/json")
            .header(Authorization, "Bearer ${SharedPref.authToken}")
        return chain.proceed(requestBuilder.build())
    }
}