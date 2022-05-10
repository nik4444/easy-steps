package com.easysteps.base

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.easysteps.R
import com.google.android.material.snackbar.Snackbar

import kotlinx.coroutines.channels.ReceiveChannel

/**
 * Created by NIKUNJ on 04-05-2022.
 */

abstract class BaseFragment<VB : ViewDataBinding>(@LayoutRes val layoutRes: Int) : Fragment() {

    var dialogBuilder: AlertDialog.Builder? = null
    var pd: AlertDialog? = null

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
        if (isShow) showProgressDialog(requireActivity()) else pd?.dismiss()
    }


    override fun onDestroy() {
        super.onDestroy()
        listSubscription.forEach { it.cancel() }
    }

    protected fun handleError(it: Throwable) {
        val activity = requireActivity()
        if (activity is BaseActivity<*>) activity.handleError(it)
    }

    private fun showProgressDialog(context: Context?) {
        dialogBuilder = AlertDialog.Builder(context)
        val layoutBuilder: View = layoutInflater.inflate(R.layout.custom_progress, null)
        val lav_actionBar: LottieAnimationView = layoutBuilder.findViewById(R.id.lav_actionBar)
        dialogBuilder?.setView(layoutBuilder)
        lav_actionBar.setAnimation(R.raw.apiloader)
        lav_actionBar.playAnimation()
        lav_actionBar.loop(true)
        pd = dialogBuilder?.create()
        pd?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        pd?.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        pd?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pd?.show()
    }

}

