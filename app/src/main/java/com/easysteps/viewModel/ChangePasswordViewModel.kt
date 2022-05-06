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
class ChangePasswordViewModel : BaseViewModel() {

    private val _changePasswordData = SingleLiveEvent<BaseResponse<Any>>()
    val changePasswordData: LiveData<BaseResponse<Any>> = _changePasswordData

    private val _changePasswordError = MutableLiveData<Throwable>()
    val dataError: LiveData<Throwable> = _changePasswordError

    fun changePassword(map: HashMap<String, Any>) {
        viewModelScope.launch {
            displayLoader()
            processDataEvent(api1Repository.changesPassword(map), onError = {
                _changePasswordError.postValue(it)
            }) {
                _changePasswordData.postValue(it)
            }
        }
    }

}