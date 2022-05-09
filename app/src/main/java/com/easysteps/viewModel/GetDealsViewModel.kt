package com.easysteps.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easysteps.base.BaseViewModel
import com.easysteps.base.SingleLiveEvent
import com.easysteps.viewModel.models.BaseResponses
import com.easysteps.viewModel.models.GetDealData
import kotlinx.coroutines.launch

/**
 * Created by NIKUNJ on 29-04-2022.
 */
class GetDealsViewModel : BaseViewModel() {

    private val _getDealsData = SingleLiveEvent<BaseResponses<GetDealData>>()
    val getDealsData: LiveData<BaseResponses<GetDealData>> = _getDealsData

    private val _getDealsError = MutableLiveData<Throwable>()
    val dataError: LiveData<Throwable> = _getDealsError

    fun getDeals() {
        viewModelScope.launch {
            displayLoader()
            processDataEvent(api1Repository.getDealsDate(), onError = {
                _getDealsError.postValue(it)
            }) {
                _getDealsData.postValue(it)
            }
        }
    }

}