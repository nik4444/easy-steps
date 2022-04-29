package com.easysteps.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.easysteps.R
import com.easysteps.helper.Utils.HideProgressDialog
import com.easysteps.helper.Utils.ShowProgressDialog
import com.easysteps.modelClases.ResponseCode
import kotlinx.coroutines.channels.ReceiveChannel
import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException


abstract class BaseActivity<VB : ViewDataBinding>(private val layoutRes: Int) :
    AppCompatActivity() {

    protected lateinit var binding: VB
    private val listSubscription = ArrayList<ReceiveChannel<*>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
    }

    override fun onDestroy() {
        super.onDestroy()
        listSubscription.forEach { it.cancel() }
    }

    open fun showMessage(message: String) {

    }

    open fun showError(errorMessage: String) {
        showMessage(errorMessage)
    }

    protected fun updateLoaderUI(isShow: Boolean) {
        if (isShow) ShowProgressDialog(this) else HideProgressDialog(this)
    }

    fun handleError(throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                handleResponseError(throwable)
            }
            is ConnectException -> {
                showMessage(getString(R.string.msg_no_internet))
            }
            is SocketTimeoutException -> {
                showMessage(getString(R.string.time_out))
            }
        }
    }

    private fun handleResponseError(throwable: HttpException) {
        when (throwable.code()) {
            ResponseCode.InvalidData.code -> {
                val errorRawData = throwable.response()?.errorBody()?.string()?.trim()
                if (!errorRawData.isNullOrEmpty()) {
                    val jsonObject = JSONObject(errorRawData)
                    val jObject = jsonObject.optJSONObject("errors")
                    if (jObject != null) {
                        val keys: Iterator<String> = jObject.keys()
                        if (keys.hasNext()) {
                            val msg = StringBuilder()
                            while (keys.hasNext()) {
                                val key: String = keys.next()
                                if (jObject.get(key) is String) {
                                    msg.append("- ${jObject.get(key)}\n")
                                }
                            }
                            errorDialog(msg.toString(), "Alert")
                        } else {
                            errorDialog(jsonObject.optString("message", ""))
                        }
                    } else {
                        errorDialog(JSONObject(errorRawData).optString("message"), "Alert")
                    }
                }
            }

            ResponseCode.InternalServerError.code -> errorDialog("Internal Server error")
            ResponseCode.BadRequest.code,
            ResponseCode.NotFound.code,
            ResponseCode.RequestTimeOut.code,
            ResponseCode.Blocked.code -> {
                val errorRawData = throwable.response()?.errorBody()?.string()?.trim()
                if (!errorRawData.isNullOrEmpty()) {
                    errorDialog(JSONObject(errorRawData).optString("message", ""))
                }
            }
        }
    }

    private fun errorDialog(optString: String?, title: String = getString(R.string.app_name)) {
        optString?.let {

        }
    }
}

