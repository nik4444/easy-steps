package com.easysteps.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easysteps.base.BaseViewModel
import com.easysteps.base.SingleLiveEvent
import com.easysteps.viewModel.models.BaseResponse
import com.easysteps.viewModel.models.SignupData
import kotlinx.coroutines.launch

/**
 * Created by NIKUNJ on 29-04-2022.
 */
class RegisterViewModel : BaseViewModel() {

    private val _loginData = SingleLiveEvent<BaseResponse<SignupData>>()
    val loginData: LiveData<BaseResponse<SignupData>> = _loginData

    private val _loginError = MutableLiveData<Throwable>()
    val dataError: LiveData<Throwable> = _loginError

    fun callApi(map: HashMap<String, Any>) {
        viewModelScope.launch {
            displayLoader()
            processDataEvent(api1Repository.register(map), onError = {
                _loginError.postValue(it)
            }) {
                _loginData.postValue(it)
            }
        }
    }

}