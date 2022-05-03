package com.easysteps.repository


import com.easysteps.api.IApiService1

class Api1Repository(private val apiService: IApiService1) : BaseRepository() {

    suspend fun login(map: HashMap<String, Any>) = callApi { apiService.login(map) }

    suspend fun register(map: HashMap<String, Any>) = callApi { apiService.register(map) }

    suspend fun forgotPassword(map: HashMap<String, Any>) = callApi { apiService.forgotPasswordPassword(map) }

    suspend fun verifyOtp(map: HashMap<String, Any>) = callApi { apiService.verifyOtp(map) }

    companion object {
        @Volatile
        private var instance: Api1Repository? = null

        fun getInstance(): Api1Repository {

            return instance ?: synchronized(this) {
                instance ?: Api1Repository(IApiService1.getService())
                    .also {
                        instance = it
                    }
            }
        }
    }
}