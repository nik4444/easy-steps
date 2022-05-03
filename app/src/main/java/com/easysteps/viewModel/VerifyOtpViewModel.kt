package com.easysteps.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easysteps.base.BaseViewModel
import com.easysteps.base.SingleLiveEvent
import com.easysteps.viewModel.models.BaseResponse
import kotlinx.coroutines.launch

/**
 * Created by NIKUNJ on 29-04-2022.
 */
class VerifyOtpViewModel : BaseViewModel() {

    private val _verifyOtpData = SingleLiveEvent<BaseResponse<Any>>()
    val verifyOtpData: LiveData<BaseResponse<Any>> = _verifyOtpData

    private val _verifyOtpError = MutableLiveData<Throwable>()
    val dataError: LiveData<Throwable> = _verifyOtpError

    fun callApi(map: HashMap<String, Any>) {
        viewModelScope.launch {
            displayLoader()
            processDataEvent(api1Repository.verifyOtp(map), onError = {
                _verifyOtpError.postValue(it)
            }) {
                _verifyOtpData.postValue(it)
            }
        }
    }

}