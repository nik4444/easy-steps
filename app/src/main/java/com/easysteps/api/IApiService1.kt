package com.easysteps.api

import com.easysteps.api.IBaseService.Companion.getOkHttpClient
import com.easysteps.helper.*
import com.easysteps.viewModel.models.*
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

    @POST(UPDATE_PROFILE)
    suspend fun updateProfile(@Body body: HashMap<String, Any>): Response<BaseResponse<ProfileUpdate>>

    @POST(LOGOUT)
    suspend fun logout(@Body body: HashMap<String, Any>): Response<BaseResponse<Any>>

    @POST(DELETE_ACCOUNT)
    suspend fun deleteAccount(@Body body: HashMap<String, Any>): Response<BaseResponse<Any>>

    @POST(CHANGE_PASSWORD)
    suspend fun changesPassword(@Body body: HashMap<String, Any>): Response<BaseResponse<Any>>

    @POST(FORGOT_PASSWORD)
    suspend fun forgotPasswordPassword(@Body body: HashMap<String, Any>): Response<BaseResponse<Any>>

    @POST(FORGOT_PASSWORD)
    suspend fun verifyOtp(@Body body: HashMap<String, Any>): Response<BaseResponse<Any>>

    @POST(REGISTER)
    suspend fun register(@Body body: HashMap<String, Any>): Response<BaseResponse<SignupData>>

    @POST(UPDATE_DAILY_STEPS)
    suspend fun updateDailyStep(): Response<BaseResponse<StepData>>

    @POST(GET_DAILY_STEPS)
    suspend fun getDailyStep(): Response<BaseResponse<GetDailyStepData>>

    @POST(ADD_DAILY_STEPS)
    suspend fun addMyDailySteps(@Body body: HashMap<String, Any>): Response<BaseResponse<StepData>>

    @POST(ADD_ACCEPT_REWARD)
    suspend fun addToAcceptReward(@Body body: HashMap<String, Any>): Response<BaseResponse<Any>>

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
