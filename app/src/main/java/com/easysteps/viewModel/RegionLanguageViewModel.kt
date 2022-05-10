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
class RegionLanguageViewModel : BaseViewModel() {

    private val _regionLanguageData = SingleLiveEvent<BaseResponse<Any>>()
    val regionLanguageData: LiveData<BaseResponse<Any>> = _regionLanguageData

    private val _regionLanguageError = MutableLiveData<Throwable>()
    val dataError: LiveData<Throwable> = _regionLanguageError

    fun callApi(map: HashMap<String, Any>) {
        viewModelScope.launch {
            displayLoader()
            processDataEvent(api1Repository.updateRegionLang(map), onError = {
                _regionLanguageError.postValue(it)
            }) {
                _regionLanguageData.postValue(it)
            }
        }
    }

}