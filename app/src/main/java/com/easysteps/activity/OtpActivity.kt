package com.easysteps.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.easysteps.R
import com.easysteps.base.BaseActivity
import com.easysteps.databinding.ActivityOtpBinding
import com.easysteps.helper.EXTRA_EMAIL
import com.easysteps.helper.RequestParamsUtils
import com.easysteps.helper.Utils
import com.easysteps.viewModel.VerifyOtpViewModel

/**
 * Created by NIKUNJ on 03-05-2022.
 */

class OtpActivity : BaseActivity<ActivityOtpBinding>(R.layout.activity_otp), View.OnClickListener {

    var email: String? = null
    val viewModel: VerifyOtpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        setUpObserver()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.tvSend.setOnClickListener(this)
    }

    private fun initView() {
        if (intent.hasExtra(EXTRA_EMAIL)) {
            email = intent.getStringExtra(EXTRA_EMAIL)
        }
    }

    private fun setUpObserver() {
        viewModel.apiErrors.observe(this) { handleError(it) }

        viewModel.appLoader.observe(this) { updateLoaderUI(it) }

        viewModel.dataError.observe(this) { handleError(it) }

        viewModel.verifyOtpData.observe(this) {
            if (it.status == 1) {
                finishAffinity()
                startActivity(Intent(this@OtpActivity, LoginActivity::class.java))
            } else Utils.MyShortSnackbar(binding.llLayout, it.message)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvSend -> {
                if (isValid()) {
                    callVerifyOtpApi()
                }
            }
        }
    }

    private fun callVerifyOtpApi() {
        val map = HashMap<String, Any>()

        map[RequestParamsUtils.userOtp] = "121212"
        map[RequestParamsUtils.userNewPass] = binding.etConfirmPassword.text.toString().trim()
        map[RequestParamsUtils.userEmail] = email.toString()

        viewModel.callApi(map)
    }

    private fun isValid(): Boolean {
        when {
            binding.etOtp.text.toString().trim().isEmpty() -> {
                Utils.MyShortSnackbar(binding.llLayout, getString(R.string.text_empty_email))
                return false
            }
            binding.etNewPassword.text.toString().trim().isEmpty() -> {
                Utils.MyShortSnackbar(binding.llLayout, getString(R.string.text_empty_mobile))
                return false
            }
            binding.etConfirmPassword.text.toString().trim().isEmpty() -> {
                Utils.MyShortSnackbar(binding.llLayout, getString(R.string.text_empty_password))
                return false
            }
            binding.etConfirmPassword.text.toString().trim() != binding.etNewPassword.text.toString().trim() -> {
                binding.etConfirmPassword.setText("")
                binding.etNewPassword.setText("")
                return false
            }
            else -> return true
        }
    }

}