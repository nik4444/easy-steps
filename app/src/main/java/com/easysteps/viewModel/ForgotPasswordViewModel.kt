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
class ForgotPasswordViewModel : BaseViewModel() {

    private val _rememberPassData = SingleLiveEvent<BaseResponse<Any>>()
    val rememberPasswordData: LiveData<BaseResponse<Any>> = _rememberPassData

    private val _rememberError = MutableLiveData<Throwable>()
    val dataError: LiveData<Throwable> = _rememberError

    fun callApi(map: HashMap<String, Any>) {
        viewModelScope.launch {
            displayLoader()
            processDataEvent(api1Repository.forgotPassword(map), onError = {
                _rememberError.postValue(it)
            }) {
                _rememberPassData.postValue(it)
            }
        }
    }

}