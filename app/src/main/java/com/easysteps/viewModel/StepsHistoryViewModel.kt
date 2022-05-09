package com.easysteps.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easysteps.base.BaseViewModel
import com.easysteps.base.SingleLiveEvent
import com.easysteps.viewModel.models.BaseResponses
import com.easysteps.viewModel.models.GetMyStepsHistoryData
import kotlinx.coroutines.launch

/**
 * Created by NIKUNJ on 29-04-2022.
 */
class StepsHistoryViewModel : BaseViewModel() {

    private val _getStepsHistoryData = SingleLiveEvent<BaseResponses<GetMyStepsHistoryData>>()
    val getStepsHistoryData: LiveData<BaseResponses<GetMyStepsHistoryData>> = _getStepsHistoryData

    private val _getStepsHistoryError = MutableLiveData<Throwable>()
    val dataError: LiveData<Throwable> = _getStepsHistoryError

    fun getStepsHistory() {
        viewModelScope.launch {
            displayLoader()
            processDataEvent(api1Repository.getStepsHistory(), onError = {
                _getStepsHistoryError.postValue(it)
            }) {
                _getStepsHistoryData.postValue(it)
            }
        }
    }

}