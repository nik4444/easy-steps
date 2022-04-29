package com.easysteps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.easysteps.fragment.SettingsFragment
import com.easysteps.helper.PrefKey
import com.easysteps.helper.Utils
import com.easysteps.retrofit.RequestParamsUtils
import com.easysteps.viewModel.LoginViewModel
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs

/**
 * Created by NIKUNJ on 29-04-2022.
 */

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login), View.OnClickListener {

    private val IS_REMEMBER = "IS_REMEMBER"
    private val IS_REMEMBER_EMAIL = "IS_REMEMBER_EMAIL"
    private val IS_REMEMBER_PASSWORD = "IS_REMEMBER_PASSWORD"
    private var isRemember = false
    private var isFaceLocked = false
    private var email: String? = null
    private var password: String? = null

    private val viewModel: LoginViewModel by viewModels()

    private fun getSwitchValue(context: Context, key: String?): Boolean {
        val settings = context.getSharedPreferences(SettingsFragment.IS_FACE_LOCK, MODE_PRIVATE)
        return settings.getBoolean(key, false)
    }

    private fun getRememberValue(context: Context, key: String?): Boolean {
        val settings = context.getSharedPreferences(IS_REMEMBER, MODE_PRIVATE)
        return settings.getBoolean(key, false)
    }

    private fun setRememberValue(context: Context, key: String?, value: Boolean) {
        val settings = context.getSharedPreferences(IS_REMEMBER, MODE_PRIVATE)
        val editor = settings.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun setRememberEmail(context: Context, key: String?, str: String?) {
        val settings = context.getSharedPreferences(IS_REMEMBER_EMAIL, MODE_PRIVATE)
        val editor = settings.edit()
        editor.putString(key, str)
        editor.apply()
    }

    private fun getRememberEmail(context: Context, key: String?): String? {
        val settings = context.getSharedPreferences(IS_REMEMBER_EMAIL, MODE_PRIVATE)
        return settings.getString(key, "")
    }

    private fun setRememberPassword(context: Context, key: String?, str: String?) {
        val settings = context.getSharedPreferences(IS_REMEMBER_PASSWORD, MODE_PRIVATE)
        val editor = settings.edit()
        editor.putString(key, str)
        editor.apply()
    }

    private fun getRememberPassword(context: Context, key: String?): String? {
        val settings = context.getSharedPreferences(IS_REMEMBER_PASSWORD, MODE_PRIVATE)
        return settings.getString(key, "")
    }

    private fun clearEmail(context: Context) {
        val preferences = context.getSharedPreferences(IS_REMEMBER_EMAIL, MODE_PRIVATE)
        val preferences2 = context.getSharedPreferences(IS_REMEMBER_PASSWORD, MODE_PRIVATE)
        val editor = preferences.edit()
        val editor2 = preferences2.edit()
        editor.clear()
        editor2.clear()
        editor.apply()
        editor2.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        setUpObserver()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.etPassword.setOnClickListener(this)
        binding.ivRemember.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)
        binding.tvForgotPassword.setOnClickListener(this)
        binding.tvLogin.setOnClickListener(this)
        binding.ivRemember.setOnClickListener(this)
    }

    private fun initView() {
        isFaceLocked = getSwitchValue(this, SettingsFragment.IS_FACE_LOCK)
        isRemember = getRememberValue(this, IS_REMEMBER)
        email = getRememberEmail(this, IS_REMEMBER_EMAIL)
        password = getRememberPassword(this, IS_REMEMBER_PASSWORD)
        val executor = ContextCompat.getMainExecutor(this)

        Log.e("TAG", " onCreate: " + Prefs.getString(PrefKey.userToken, "test"))

        val biometricPrompt = BiometricPrompt(this@LoginActivity,
            executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Log.e("TAG", "onAuthenticationError: $errString")
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
            binding.etEmail.setText(email)
            binding.etPassword.setText(password)
        }
        if (isFaceLocked) {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun setUpObserver() {
        viewModel.apiErrors.observe(this) { handleError(it) }

        viewModel.appLoader.observe(this) { updateLoaderUI(it) }

        viewModel.dataError.observe(this) { it.printStackTrace() }

        viewModel.loginData.observe(this) {
            Prefs.putString(PrefKey.login_info, Gson().toJson(it.data))
            Utils.setLoginStatus(true)
            Utils.setUserToken(it.data.userToken)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.ivRemember) {
            if (isRemember) {
                isRemember = false
                setRememberValue(this, IS_REMEMBER, false)
                clearEmail(this)
                binding.ivRemember.setImageResource(R.drawable.ic_check)
            } else {
                isRemember = true
                setRememberValue(this, IS_REMEMBER, true)
                binding.ivRemember.setImageResource(R.drawable.ic_checked)
                setRememberEmail(this, IS_REMEMBER_EMAIL, binding.etEmail.text.toString().trim())
                setRememberPassword(this, IS_REMEMBER_PASSWORD, binding.etPassword!!.text.toString().trim())
            }
        } else if (v.id == R.id.tvRegister) {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        } else if (v.id == R.id.tvForgotPassword) {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        } else if (v.id == R.id.tvLogin) {
            if (binding.etEmail.text.toString().isEmpty()) {
                Utils.MyShortToast(this, getString(R.string.text_empty_email))
            } else if (binding.etPassword.text.toString().isEmpty()) {
                Utils.MyShortSnackbar(binding.llLogin, getString(R.string.text_empty_password))
            } else if (!Utils.isValidEmail(binding.etEmail.text.toString().trim())) {
                Utils.MyShortSnackbar(binding.llLogin, getString(R.string.text_valid_email))
            } else if (!Utils.isValidPassword(binding.etPassword.text.toString().trim())) {
                Utils.MyShortSnackbar(binding.llLogin, getString(R.string.text_valid_password))
            } else {
                callApi()
            }
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