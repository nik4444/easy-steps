package com.easysteps.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easysteps.base.BaseViewModel
import com.easysteps.base.SingleLiveEvent
import com.easysteps.viewModel.models.BaseResponse
import com.easysteps.viewModel.models.ProfileUpdate
import kotlinx.coroutines.launch

/**
 * Created by NIKUNJ on 29-04-2022.
 */
class UpdateProfileViewModel : BaseViewModel() {

    private val _updateProfileData = SingleLiveEvent<BaseResponse<ProfileUpdate>>()
    val updateProfileData: LiveData<BaseResponse<ProfileUpdate>> = _updateProfileData

    private val _updateProfileError = MutableLiveData<Throwable>()
    val dataError: LiveData<Throwable> = _updateProfileError

    fun updateProfile(map: HashMap<String, Any>) {
        viewModelScope.launch {
            displayLoader()
            processDataEvent(api1Repository.updateProfile(map), onError = {
                _updateProfileError.postValue(it)
            }) {
                _updateProfileData.postValue(it)
            }
        }
    }

}