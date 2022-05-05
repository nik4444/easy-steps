package com.easysteps.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easysteps.base.BaseViewModel
import com.easysteps.base.SingleLiveEvent
import com.easysteps.viewModel.models.BaseResponse
import com.easysteps.viewModel.models.GetDailyStepData
import com.easysteps.viewModel.models.StepData
import kotlinx.coroutines.launch

/**
 * Created by NIKUNJ on 29-04-2022.
 */
class HomeViewModel : BaseViewModel() {

    private val _updateStepData = SingleLiveEvent<BaseResponse<StepData>>()
    val updateStepData: LiveData<BaseResponse<StepData>> = _updateStepData
    private val _updateError = MutableLiveData<Throwable>()
    val updateStepError: LiveData<Throwable> = _updateError

    private val _getStepData = SingleLiveEvent<BaseResponse<GetDailyStepData>>()
    val getStepData: LiveData<BaseResponse<GetDailyStepData>> = _getStepData
    private val _getError = MutableLiveData<Throwable>()
    val getStepError: LiveData<Throwable> = _getError

    private val _addDailyStepData = SingleLiveEvent<BaseResponse<StepData>>()
    val addDailyStepData: LiveData<BaseResponse<StepData>> = _addDailyStepData
    private val _getAddDailyStepError = MutableLiveData<Throwable>()
    val addDailyStepError: LiveData<Throwable> = _getAddDailyStepError

    private val _addToAcceptStepData = SingleLiveEvent<BaseResponse<Any>>()
    val addToAcceptRewardData: LiveData<BaseResponse<Any>> = _addToAcceptStepData
    private val _getAddToAcceptError = MutableLiveData<Throwable>()
    val addToAcceptRewardError: LiveData<Throwable> = _getAddToAcceptError


    fun updateDailySteps() {
        viewModelScope.launch {
            displayLoader()
            processDataEvent(api1Repository.updateDailyStep(), onError = {
                _updateError.postValue(it)
            }) {
                _updateStepData.postValue(it)
            }
        }
    }

    fun getDailySteps() {
        viewModelScope.launch {
            displayLoader()
            processDataEvent(api1Repository.getDailyStep(), onError = {
                _getError.postValue(it)
            }) {
                _getStepData.postValue(it)
            }
        }
    }

    fun addDailySteps(map: HashMap<String, Any>) {
        viewModelScope.launch {
            displayLoader()
            processDataEvent(api1Repository.addDailyStep(map), onError = {
                _getAddDailyStepError.postValue(it)
            }) {
                _addDailyStepData.postValue(it)
            }
        }
    }

    fun addToAcceptReward(map: HashMap<String, Any>) {
        viewModelScope.launch {
            displayLoader()
            processDataEvent(api1Repository.addToAcceptReward(map), onError = {
                _getAddToAcceptError.postValue(it)
            }) {
                _addToAcceptStepData.postValue(it)
            }
        }
    }

}