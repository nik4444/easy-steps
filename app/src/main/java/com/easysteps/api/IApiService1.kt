package com.easysteps.api

import com.easysteps.api.IBaseService.Companion.getOkHttpClient
import com.easysteps.helper.BASE_URL
import com.easysteps.helper.FORGOT_PASSWORD
import com.easysteps.helper.LOGIN
import com.easysteps.viewModel.models.BaseResponse
import com.easysteps.viewModel.models.SignupData
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface IApiService1 : IBaseService {

    @POST(LOGIN)
    suspend fun login(@Body body: HashMap<String, Any>): Response<BaseResponse<SignupData>>

    @POST(FORGOT_PASSWORD)
    suspend fun forgotPasswordPassword(@Body body: HashMap<String, Any>): Response<BaseResponse<Any>>

    companion object {
        fun getService(): IApiService1 {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setLenient().create()
                    )
                )
                .addConverterFactory(ScalarsConverterFactory.create())
                .build().create(IApiService1::class.java)
        }
    }
}
