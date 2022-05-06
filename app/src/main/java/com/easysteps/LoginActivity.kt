package com.easysteps

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import com.easysteps.activity.ForgotPasswordActivity
import com.easysteps.activity.MainActivity
import com.easysteps.activity.RegisterActivity
import com.easysteps.base.BaseActivity
import com.easysteps.databinding.ActivityLoginBinding
import com.easysteps.helper.PrefKey
import com.easysteps.helper.Utils
import com.easysteps.pref.SharedPref
import com.easysteps.pref.SharedPref.isRemember
import com.easysteps.pref.SharedPref.rememberEmail
import com.easysteps.pref.SharedPref.rememberPassword
import com.easysteps.retrofit.RequestParamsUtils
import com.easysteps.viewModel.LoginViewModel
import com.pixplicity.easyprefs.library.Prefs
import timber.log.Timber

/**
 * Created by NIKUNJ on 29-04-2022.
 */

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login), View.OnClickListener {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        setUpObserver()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.ivRemember.setOnClickListener(this)
        binding.tvRemember.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)
        binding.tvForgotPassword.setOnClickListener(this)
        binding.tvLogin.setOnClickListener(this)
    }

    private fun initView() {
        val executor = ContextCompat.getMainExecutor(this)

        Timber.e(" onCreate: " + Prefs.getString(PrefKey.userToken, "test"))

        val biometricPrompt = BiometricPrompt(this@LoginActivity,
            executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Timber.e("onAuthenticationError: $errString")
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            })

        val promptInfo = PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using fingerprint or face")
            .setNegativeButtonText("Cancel")
            .build()

        if (isRemember) {
            binding.ivRemember.setImageResource(R.drawable.ic_checked)
            binding.etEmail.setText(rememberEmail)
            binding.etPassword.setText(rememberPassword)
        }
//        if (isFaceLocked) {
//            biometricPrompt.authenticate(promptInfo)
//        }
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
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else Utils.MyShortSnackbar(binding.llLogin, "" + it.message)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivRemember, R.id.tvRemember -> {
                if (isRemember) {
                    isRemember = false
                    rememberEmail = ""
                    rememberPassword = ""
                    binding.ivRemember.setImageResource(R.drawable.ic_check)
                } else {
                    isRemember = true
                    binding.ivRemember.setImageResource(R.drawable.ic_checked)
                    rememberEmail = binding.etEmail.text.toString().trim()
                    rememberPassword = binding.etPassword.text.toString().trim()
                }
            }
            R.id.tvRegister -> startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))

            R.id.tvForgotPassword -> startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))

            R.id.tvLogin -> if (isValid()) callApi()
        }
    }

    private fun isValid(): Boolean {
        when {
            binding.etEmail.text.toString().isEmpty() -> {
                Utils.MyShortToast(this, getString(R.string.text_empty_email))
                return false
            }
            binding.etPassword.text.toString().isEmpty() -> {
                Utils.MyShortSnackbar(binding.llLogin, getString(R.string.text_empty_password))
                return false
            }
            !Utils.isValidEmail(binding.etEmail.text.toString().trim()) -> {
                Utils.MyShortSnackbar(binding.llLogin, getString(R.string.text_valid_email))
                return false
            }
            !Utils.isValidPassword(binding.etPassword.text.toString().trim()) -> {
                Utils.MyShortSnackbar(binding.llLogin, getString(R.string.text_valid_password))
                return false
            }
            else -> return true

        }
    }

    private fun callApi() {
        val map = HashMap<String, Any>()
        map[RequestParamsUtils.userEmail] = binding.etEmail.text.toString().trim()
        map[RequestParamsUtils.userPassword] = binding.etPassword.text.toString().trim()
        map[RequestParamsUtils.userLatitude] = "21.132564"
        map[RequestParamsUtils.userLongitude] = "12.123456"
        map[RequestParamsUtils.userDeviceToken] = Prefs.getString(PrefKey.token, "testToken")
        map[RequestParamsUtils.userDevice] = "1"
        map[RequestParamsUtils.userDeviceId] = Utils.getDeviceId(this)
        viewModel.callApi(map)
    }
}