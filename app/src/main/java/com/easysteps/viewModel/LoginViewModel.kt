package com.easysteps.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easysteps.base.BaseViewModel
import com.easysteps.base.SingleLiveEvent
import com.easysteps.viewModel.models.Login
import kotlinx.coroutines.launch

/**
 * Created by NIKUNJ on 29-04-2022.
 */
class LoginViewModel : BaseViewModel() {

    private val _loginData = SingleLiveEvent<Login>()
    val loginData: LiveData<Login> = _loginData

    private val _loginError = MutableLiveData<Throwable>()
    val dataError: LiveData<Throwable> = _loginError

    fun callApi(map: HashMap<String, Any>) {
        viewModelScope.launch {
            displayLoader()
            processDataEvent(api1Repository.login(map), onError = {
                _loginError.postValue(it)
            }) {
                _loginData.postValue(it)
            }
        }
    }

}