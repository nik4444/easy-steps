package com.easysteps.repository

import com.easysteps.base.ApiError
import com.easysteps.base.ApiEvent
import com.easysteps.base.ApiSuccess

import com.easysteps.modelClases.ResponseCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

abstract class BaseRepository {

    protected suspend fun <T> callApi(apiCall: suspend () -> Response<T>): ApiEvent<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                //Listen APIs Response/Failure in @Dispatchers.Main thread
                if (response.code() == ResponseCode.OK.code)
                    withContext(Dispatchers.Main) { ApiSuccess(response.body()) }
                else
                    throw HttpException(response)
            } catch (e: Exception) {
                ApiError(e)
            }
        }
    }
}