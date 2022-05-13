package com.easysteps.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.easysteps.R
import com.easysteps.base.BaseActivity
import com.easysteps.databinding.ActivityForgotPasswordBinding
import com.easysteps.helper.EXTRA_EMAIL
import com.easysteps.helper.RequestParamsUtils
import com.easysteps.helper.Utils
import com.easysteps.viewModel.ForgotPasswordViewModel

/**
 * Created by NIKUNJ on 02-05-2022.
 */
class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding>(R.layout.activity_forgot_password),
    View.OnClickListener {

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpObserver()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.tvSend.setOnClickListener(this)
    }

    private fun setUpObserver() {
        viewModel.apiErrors.observe(this) { handleError(it) }

        viewModel.appLoader.observe(this) { updateLoaderUI(it) }

        viewModel.dataError.observe(this) { handleError(it) }

        viewModel.rememberPasswordData.observe(this) {
            if (it.status == 1) {
                val intent = Intent(this@ForgotPasswordActivity, OtpActivity::class.java)
                intent.putExtra(EXTRA_EMAIL, binding.etEmail.text.toString().trim())
                startActivity(intent)
            } else Utils.MyShortSnackbar(binding.llForgot, it.message)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvSend -> {
                if (!Utils.validEmail(binding.etEmail.text.toString().trim())) {
                    Toast.makeText(this, "Enter valid email!", Toast.LENGTH_LONG).show()
                } else {
                    val map = HashMap<String, Any>()
                    map[RequestParamsUtils.userEmail] = binding.etEmail.text.toString().trim()
                    viewModel.callApi(map)
                }
            }
        }
    }
}