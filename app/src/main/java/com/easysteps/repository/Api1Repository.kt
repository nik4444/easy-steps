package com.easysteps.repository


import com.easysteps.api.IApiService1

class Api1Repository(private val apiService: IApiService1) : BaseRepository() {

    suspend fun login(map: HashMap<String, Any>) = callApi { apiService.login(map) }

    suspend fun register(map: HashMap<String, Any>) = callApi { apiService.register(map) }

    suspend fun forgotPassword(map: HashMap<String, Any>) = callApi { apiService.forgotPasswordPassword(map) }

    suspend fun verifyOtp(map: HashMap<String, Any>) = callApi { apiService.verifyOtp(map) }

    suspend fun updateDailyStep() = callApi { apiService.updateDailyStep() }

    suspend fun getDailyStep() = callApi { apiService.getDailyStep() }

    suspend fun addDailyStep(map: HashMap<String, Any>) = callApi { apiService.addMyDailySteps(map) }

    suspend fun addToAcceptReward(map: HashMap<String, Any>) = callApi { apiService.addToAcceptReward(map) }

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