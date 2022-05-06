package com.easysteps.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.easysteps.LoginActivity
import com.easysteps.R
import com.easysteps.base.BaseFragment
import com.easysteps.databinding.FragmentSettingsBinding
import com.easysteps.pref.SharedPref
import com.easysteps.pref.SharedPref.isFaceLocked
import com.easysteps.retrofit.RequestParamsUtils
import com.easysteps.viewModel.SettingViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import timber.log.Timber

/**
 * Created by NIKUNJ on 06-05-2022.
 */
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings), View.OnClickListener {

    private var fitnessOptions: FitnessOptions? = null
    private val viewModel: SettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpObserver()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.llProfile.setOnClickListener(this)
        binding.llLanguage.setOnClickListener(this)
        binding.llLearnEasySteps.setOnClickListener(this)
        binding.llContactUs.setOnClickListener(this)
        binding.llRateApp.setOnClickListener(this)
        binding.llTermCondition.setOnClickListener(this)
        binding.llPolicy.setOnClickListener(this)
        binding.llChangePassword.setOnClickListener(this)
        binding.llDeleteAccount.setOnClickListener(this)
        binding.llLogout.setOnClickListener(this)
    }

    private fun setUpObserver() {
        viewModel.apiErrors.observe(requireActivity()) { handleError(it) }

        viewModel.appLoader.observe(requireActivity()) { updateLoaderUI(it) }

        viewModel.dataError.observe(requireActivity()) { it.printStackTrace() }

        viewModel.logoutData.observe(requireActivity()) {
            if (it.status == 1) {
                requireActivity().finishAffinity()
                SharedPref.clearPref()
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }

        viewModel.removeAccountError.observe(requireActivity()) { it.printStackTrace() }
        viewModel.removeAccountData.observe(requireActivity()) {
            if (it.status == 1) {
                requireActivity().finishAffinity()
                SharedPref.clearPref()
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
    }

    private fun initView() {
        binding.faceSwitch.isChecked = isFaceLocked

        fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()

        binding.faceSwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            isFaceLocked = isChecked
        }

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.llProfile -> {
                val profileFragment: Fragment = ProfileFragment()
                loadChildFragment(profileFragment)
            }
            R.id.llLanguage -> {
                val languageFragment: Fragment = ReginLanguageFragment()
                loadChildFragment(languageFragment)
            }
            R.id.llLearnEasySteps -> {
            }
            R.id.llContactUs -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + requireActivity().getString(R.string.text_mail)))
                startActivity(intent)
            }
            R.id.llRateApp -> {
                val uri: Uri = Uri.parse("market://details?id=" + requireActivity().packageName)
                val goMarket = Intent(Intent.ACTION_VIEW, uri)
                startActivity(goMarket)
            }
            R.id.llTermCondition -> {
                val termIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://46.101.95.217/easysteps/terms-condition"))
                startActivity(termIntent)
            }
            R.id.llPolicy -> {
                val policyIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://46.101.95.217/easysteps/privacy-policy"))
                startActivity(policyIntent)
            }
            R.id.llChangePassword -> {
                val changePasswordFragment: Fragment = ChangePasswordFragment()
                loadChildFragment(changePasswordFragment)
            }
            R.id.llDeleteAccount -> {
                val llConfirmDelete: LinearLayout?
                val llCancelDelete: LinearLayout?

                val deleteDialog = Dialog(requireContext())
                deleteDialog.setContentView(R.layout.dialog_delete)
                deleteDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                deleteDialog.show()

                llConfirmDelete = deleteDialog.findViewById(R.id.ll_confirm_delete)
                llCancelDelete = deleteDialog.findViewById(R.id.ll_cancel_delete)

                llConfirmDelete.setOnClickListener(View.OnClickListener {
                    Fitness.getRecordingClient(
                        requireActivity(),
                        GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions!!)
                    )
                        .unsubscribe(DataType.TYPE_STEP_COUNT_DELTA)
                        .addOnSuccessListener {
                            val gso =
                                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                            val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                            googleSignInClient.signOut()
                        }
                        .addOnFailureListener { e: java.lang.Exception ->
                            Timber.e("Not Disconnect: " + e.message)
                        }
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                    val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                    googleSignInClient.signOut()
                    deleteAccount()
                    deleteDialog.dismiss()
                })
                llCancelDelete.setOnClickListener(View.OnClickListener { deleteDialog.dismiss() })
            }
            R.id.llLogout -> {
                AlertDialog.Builder(activity)
                    .setTitle(R.string.dialog_logout)
                    .setMessage(R.string.dialog_logout_message)
                    .setCancelable(false)
                    .setNegativeButton(R.string.dialog_no) { _, _ -> }
                    .setPositiveButton(R.string.dialog_yes) { _, _ ->
                        Fitness.getRecordingClient(
                            requireActivity(),
                            GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions!!)
                        )
                            .unsubscribe(DataType.TYPE_STEP_COUNT_DELTA)
                            .addOnSuccessListener {
                                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                                val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                                googleSignInClient.signOut()
                            }
                            .addOnFailureListener { e: Exception ->
                                Timber.e("Not Disconnect: " + e.message)
                            }
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                        googleSignInClient.signOut()
                        logOut()
                    }.show()
            }
        }
    }

    private fun deleteAccount() {
        val map = HashMap<String, Any>()
        map[RequestParamsUtils.user_Id] = SharedPref.signupData?.userId.toString()
        map[RequestParamsUtils.device_id] = SharedPref.signupData?.userDeviceId.toString()
        viewModel.callDeleteAccount(map)
    }

    private fun logOut() {
        val map = HashMap<String, Any>()
        map[RequestParamsUtils.user_Id] = SharedPref.signupData?.userId.toString()
        map[RequestParamsUtils.device_id] = SharedPref.signupData?.userDeviceId.toString()
        viewModel.callLogoutApi(map)
    }

    private fun loadChildFragment(nextFragment: Fragment) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.setting_frame_container, nextFragment)
            .addToBackStack(nextFragment.toString())
            .commit()
    }

}