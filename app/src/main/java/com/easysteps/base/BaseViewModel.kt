package com.easysteps.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.easysteps.repository.Api1Repository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel(), CoroutineScope {

    protected val api1Repository: Api1Repository by lazy { Api1Repository.getInstance() }

    private val errorHandler = CoroutineExceptionHandler { _, throwable -> Timber.w(throwable) }
    private val vmJob = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.IO + vmJob + errorHandler

    private val _apiErrors = MutableLiveData<Throwable>()
    val apiErrors: LiveData<Throwable> get() = _apiErrors

    private val _appLoader = MutableLiveData<Boolean>()
    val appLoader: LiveData<Boolean> get() = _appLoader


    protected inline fun <T> processDataEvent(
        result: Event<T>,
        onError: (value: Throwable) -> Unit = {},
        onSuccess: (value: T) -> Unit
    ) {
        when (result) {
            is ApiEvent -> handleApiEvent(result, onSuccess, onError)
        }
    }

    protected inline fun <T> handleApiEvent(
        result: ApiEvent<T>,
        onSuccess: (value: T) -> Unit,
        onError: (value: Throwable) -> Unit
    ) {
        try {
            when (result) {
                is ApiError -> {
                    dismissLoader()
                    showError(result.error)
                    onError(result.error)
                }
                is ApiSuccess -> {
                    dismissLoader()
                    result.response?.let { onSuccess(it) }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected open fun showError(error: Throwable) {
        _apiErrors.value = error
    }

    private var loaderCount = 0

    fun dismissLoader() {
        loaderCount--
        if (loaderCount <= 0) {
            loaderCount = 0
            _appLoader.value = false
        }
    }

    fun displayLoader() {
        loaderCount++
        if (loaderCount == 1) _appLoader.value = true
    }

}