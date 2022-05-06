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
class SettingViewModel : BaseViewModel() {

    private val _logoutData = SingleLiveEvent<BaseResponse<Any>>()
    val logoutData: LiveData<BaseResponse<Any>> = _logoutData

    private val _logoutError = MutableLiveData<Throwable>()
    val dataError: LiveData<Throwable> = _logoutError

    private val _removeAccountData = SingleLiveEvent<BaseResponse<Any>>()
    val removeAccountData: LiveData<BaseResponse<Any>> = _removeAccountData

    private val _removeAccountError = MutableLiveData<Throwable>()
    val removeAccountError: LiveData<Throwable> = _removeAccountError

    fun callLogoutApi(map: HashMap<String, Any>) {
        viewModelScope.launch {
            displayLoader()
            processDataEvent(api1Repository.logout(map), onError = {
                _logoutError.postValue(it)
            }) {
                _logoutData.postValue(it)
            }
        }
    }

    fun callDeleteAccount(map: HashMap<String, Any>) {
        viewModelScope.launch {
            displayLoader()
            processDataEvent(api1Repository.deleteAccount(map), onError = {
                _removeAccountError.postValue(it)
            }) {
                _removeAccountData.postValue(it)
            }
        }
    }

}