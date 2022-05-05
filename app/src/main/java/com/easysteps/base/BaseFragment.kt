package com.easysteps.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.easysteps.helper.Utils
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * Created by NIKUNJ on 04-05-2022.
 */

abstract class BaseFragment<VB : ViewDataBinding>(@LayoutRes val layoutRes: Int) : Fragment() {

    protected lateinit var binding: VB
    protected val listSubscription = ArrayList<ReceiveChannel<*>>()
    val handler: Handler by lazy { Handler(Looper.getMainLooper()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        return binding.root
    }

    open fun showMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    open fun showError(errorMessage: String) {
        showMessage(errorMessage)
    }

    protected fun updateLoaderUI(isShow: Boolean) {
        if (isShow) Utils.ShowProgressDialog(requireActivity()) else Utils.HideProgressDialog(requireActivity())
    }


    override fun onDestroy() {
        super.onDestroy()
        listSubscription.forEach { it.cancel() }
    }

    protected fun handleError(it: Throwable) {
        val activity = requireActivity()
        if (activity is BaseActivity<*>) activity.handleError(it)
    }
}

