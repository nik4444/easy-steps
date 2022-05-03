package com.easysteps.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.easysteps.LoginActivity
import com.easysteps.R
import com.easysteps.base.BaseActivity
import com.easysteps.databinding.ActivityRegisterBinding
import com.easysteps.helper.Utils
import com.easysteps.pref.SharedPref
import com.easysteps.retrofit.RequestParamsUtils
import com.easysteps.viewModel.RegisterViewModel

/**
 * Created by NIKUNJ on 03-05-2022.
 */
class RegisterActivity : BaseActivity<ActivityRegisterBinding>(R.layout.activity_register), View.OnClickListener {

    val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpObserver()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.tvRegister.setOnClickListener(this)
        binding.tvLogin.setOnClickListener(this)
    }

    private fun setUpObserver() {
        viewModel.apiErrors.observe(this) { handleError(it) }

        viewModel.appLoader.observe(this) { updateLoaderUI(it) }

        viewModel.dataError.observe(this) { it.printStackTrace() }

        viewModel.loginData.observe(this) {
            if (it.status == 1) {
                SharedPref.signupData = it.data
                SharedPref.isLogin = true
                SharedPref.authToken = it.data?.userToken.toString()
                finishAffinity()
                startActivity(Intent(this, MainActivity::class.java))
            } else Utils.MyShortSnackbar(binding.llLayout, "" + it.message)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvRegister -> {
                if (isValid()) {
                    SharedPref.registerName = binding.etFullName.text.toString()
                    signUpApi()
                }
            }
            R.id.tvLogin -> {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun signUpApi() {
        val map = HashMap<String, Any>()

        map[RequestParamsUtils.userName] = binding.etFullName.text.toString().trim()
        map[RequestParamsUtils.userEmail] = binding.etEmail.text.toString().trim()
        map[RequestParamsUtils.userPhone] = binding.etPhone.text.toString().trim()
        map[RequestParamsUtils.userPassword] = binding.etPassword.text.toString().trim()

        map[RequestParamsUtils.userLatitude] = "21.132564"
        map[RequestParamsUtils.userLongitude] = "12.123456"
        map[RequestParamsUtils.userDeviceToken] = SharedPref.authToken
        map[RequestParamsUtils.userDevice] = "1"
        map[RequestParamsUtils.userDeviceId] = Utils.getDeviceId(this)

        viewModel.callApi(map)
    }

    private fun isValid(): Boolean {
        when {
            binding.etFullName.text.toString().isEmpty() -> {
                Utils.MyShortSnackbar(binding.llLayout, getString(R.string.text_empty_username))
                return false
            }
            binding.etEmail.text.toString().isEmpty() -> {
                Utils.MyShortSnackbar(binding.llLayout, getString(R.string.text_empty_email))
                return false
            }
            binding.etPhone.text.toString().isEmpty() -> {
                Utils.MyShortSnackbar(binding.llLayout, getString(R.string.text_empty_mobile))
                return false
            }
            binding.etPassword.text.toString().isEmpty() -> {
                Utils.MyShortSnackbar(binding.llLayout, getString(R.string.text_empty_password))
                return false
            }
            !Utils.isValidEmail(binding.etEmail.text.toString().trim()) -> {
                Utils.MyShortSnackbar(binding.llLayout, getString(R.string.text_valid_email))
                return false
            }
            !Utils.isValidPassword(binding.etPassword.text.toString().trim()) -> {
                Utils.MyShortSnackbar(binding.llLayout, getString(R.string.text_valid_password))
                return false
            }
            else -> return true
        }
    }
}