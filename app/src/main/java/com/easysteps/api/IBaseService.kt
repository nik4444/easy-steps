package com.easysteps.api

import com.easysteps.BuildConfig
import com.easysteps.base.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

interface IBaseService {
    companion object {
        const val Accept = "Accept"
        const val Authorization = "Authorization"
        private const val TIME_OUT = 120L

        fun getOkHttpClient(): OkHttpClient {
            val httpClient = OkHttpClient.Builder()
            httpClient.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            httpClient.readTimeout(TIME_OUT, TimeUnit.SECONDS)
            httpClient.writeTimeout(TIME_OUT, TimeUnit.SECONDS)

            httpClient.addInterceptor(AuthInterceptor())

            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.apply { logging.level = HttpLoggingInterceptor.Level.BODY }
                httpClient.addInterceptor(logging)
            }
            return httpClient.build()
        }
    }
}