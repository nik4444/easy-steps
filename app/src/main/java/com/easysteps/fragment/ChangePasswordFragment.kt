package com.easysteps.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.easysteps.R
import com.easysteps.base.BaseFragment
import com.easysteps.databinding.FragmentChangePasswordBinding
import com.easysteps.helper.Utils
import com.easysteps.retrofit.RequestParamsUtils
import com.easysteps.viewModel.ChangePasswordViewModel

/**
 * Created by NIKUNJ on 06-05-2022.
 */
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>(R.layout.fragment_change_password),
    View.OnClickListener {

    val viewModel: ChangePasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpObserver()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.imgBack.setOnClickListener(this)
        binding.txtSave.setOnClickListener(this)

    }

    private fun setUpObserver() {

        viewModel.apiErrors.observe(requireActivity()) { handleError(it) }

        viewModel.appLoader.observe(requireActivity()) { updateLoaderUI(it) }

        viewModel.dataError.observe(requireActivity()) { it.printStackTrace() }

        viewModel.changePasswordData.observe(requireActivity()) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            val manager = parentFragmentManager
            manager.popBackStackImmediate()
        }
    }

    private fun initView() {

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.txtSave -> {
                if (isValid()) {
                    val map = HashMap<String, Any>()
                    map[RequestParamsUtils.CurrentPassword] = binding.edtCurrentPassword.text.toString().trim()
                    map[RequestParamsUtils.NewPassword] = binding.edtConfirmPassword.text.toString().trim()
                    viewModel.changePassword(map)
                }
            }
            R.id.imgBack -> {
                val manager = parentFragmentManager
                manager.popBackStackImmediate()
            }
        }
    }

    private fun isValid(): Boolean {
        when {
            binding.edtCurrentPassword.text.toString().trim().isEmpty() -> {
                Toast.makeText(requireContext(), getString(R.string.currentPassword), Toast.LENGTH_SHORT).show()
                return false
            }
            binding.edtNewPassword.text.toString().trim().isEmpty() -> {
                Toast.makeText(requireContext(), getString(R.string.newPassword), Toast.LENGTH_SHORT).show()
                return false
            }
            binding.edtConfirmPassword.text.toString().trim().isEmpty() -> {
                Toast.makeText(requireContext(), getString(R.string.confirmPassword), Toast.LENGTH_SHORT).show()
                return false
            }
            binding.edtNewPassword.text.toString().trim() != binding.edtConfirmPassword.text.toString().trim() -> {
                Toast.makeText(requireContext(), getString(R.string.passwordMatch), Toast.LENGTH_SHORT).show()
                binding.edtNewPassword.setText("")
                binding.edtConfirmPassword.setText("")
                return false
            }
            !Utils.isValidPassword(binding.edtNewPassword.text.toString().trim()) -> {
                Toast.makeText(requireContext(), getString(R.string.text_valid_password), Toast.LENGTH_SHORT).show()
                binding.edtNewPassword.setText("")
                binding.edtConfirmPassword.setText("")
                return false
            }
            else -> return true
        }
    }

}