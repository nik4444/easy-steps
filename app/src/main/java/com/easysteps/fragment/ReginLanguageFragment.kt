package com.easysteps.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import com.easysteps.R
import com.easysteps.activity.MainActivity
import com.easysteps.base.BaseFragment
import com.easysteps.databinding.FragmentReginLanguageBinding
import com.easysteps.helper.RequestParamsUtils
import com.easysteps.pref.SharedPref.language
import com.easysteps.pref.SharedPref.region
import com.easysteps.pref.SharedPref.selectedLang
import com.easysteps.viewModel.RegionLanguageViewModel

/**
 * Created by NIKUNJ on 10-05-2022.
 */
class ReginLanguageFragment : BaseFragment<FragmentReginLanguageBinding>(R.layout.fragment_regin_language), View.OnClickListener {

    val viewModel: RegionLanguageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpObserver()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.llRegion.setOnClickListener(this)
        binding.llLanguage.setOnClickListener(this)
        binding.icBack.setOnClickListener(this)
    }

    private fun setUpObserver() {

        viewModel.apiErrors.observe(requireActivity()) { handleError(it) }

        viewModel.appLoader.observe(requireActivity()) { updateLoaderUI(it) }

        viewModel.dataError.observe(requireActivity()) { it.printStackTrace() }

        viewModel.regionLanguageData.observe(requireActivity()) {
            if (region != "" && binding.txtLanguage.text != "") changeLanguage()
        }
    }

    private fun initView() {
        binding.txtCountry.text = region
        binding.txtLanguage.text = language
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.llRegion -> {
                showRegion()
            }
            R.id.llLanguage -> {
                showLanguage()
            }
            R.id.icBack -> {
                val manager = parentFragmentManager
                manager.popBackStackImmediate()
            }
        }

    }

    private fun showLanguage() {
        val languageDialog = Dialog(requireActivity(), android.R.style.Theme_Dialog)
        languageDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        languageDialog.setContentView(R.layout.language_dialog)

        val llArabic = languageDialog.findViewById<LinearLayout>(R.id.llArabic)
        val llEnglish = languageDialog.findViewById<LinearLayout>(R.id.llEnglish)
        val llPortuguese = languageDialog.findViewById<LinearLayout>(R.id.llPortuguese)
        val llSpanish = languageDialog.findViewById<LinearLayout>(R.id.llSpanish)

        llArabic.setOnClickListener {
            language = requireActivity().getString(R.string.arabic_language)
            binding.txtLanguage.text = language
            language = language
            selectedLang = "ar"
            updateRegionLang()
            languageDialog.dismiss()
        }

        llEnglish.setOnClickListener {
            language = requireActivity().getString(R.string.english_language)
            binding.txtLanguage.text = language
            language = language

            updateRegionLang()
            selectedLang = "en"
            languageDialog.dismiss()
        }

        llPortuguese.setOnClickListener {
            language = requireActivity().getString(R.string.portuguese_language)
            binding.txtLanguage.text = language
            language = language
            updateRegionLang()
            selectedLang = "pt"
            languageDialog.dismiss()
        }

        llSpanish.setOnClickListener {
            language = requireActivity().getString(R.string.spanish_language)
            binding.txtLanguage.text = language
            language = language
            updateRegionLang()
            selectedLang = "es"
            languageDialog.dismiss()
        }

        languageDialog.show()
    }

    private fun changeLanguage() {
        (requireContext() as MainActivity).setNewLocale()
    }

    private fun updateRegionLang() {
        val map = HashMap<String, Any>()
        map[RequestParamsUtils.userRegion] = region
        map[RequestParamsUtils.userLanguage] = language
        viewModel.callApi(map)
    }

    private fun showRegion() {
        val regionDialog = Dialog(requireContext(), android.R.style.Theme_Dialog)
        regionDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        regionDialog.setContentView(R.layout.region_dialog)

        val llUsa = regionDialog.findViewById<LinearLayout>(R.id.llUsa)
        val llUk = regionDialog.findViewById<LinearLayout>(R.id.llUk)
        val llBrazilian = regionDialog.findViewById<LinearLayout>(R.id.llBrazilian)
        val llArgentina = regionDialog.findViewById<LinearLayout>(R.id.llArgentina)
        val llUae = regionDialog.findViewById<LinearLayout>(R.id.llUae)

        llUsa.setOnClickListener {
            region = requireActivity().getString(R.string.usa_language)
            binding.txtCountry.text = region
            region = region

            updateRegionLang()

            regionDialog.dismiss()
        }
        llUk.setOnClickListener {
            region = requireActivity().getString(R.string.uk_language)
            binding.txtCountry.text = region
            region = region
            updateRegionLang()
            regionDialog.dismiss()
        }
        llBrazilian.setOnClickListener {
            region = requireActivity().getString(R.string.brazilian_language)
            binding.txtCountry.text = region
            region = region
            updateRegionLang()
            regionDialog.dismiss()
        }
        llArgentina.setOnClickListener {
            region = requireActivity().getString(R.string.argentina_language)
            binding.txtCountry.text = region
            region = region
            updateRegionLang()
            regionDialog.dismiss()
        }
        llUae.setOnClickListener {
            region = requireActivity().getString(R.string.uae_language)
            binding.txtCountry.text = region
            region = region
            updateRegionLang()
            regionDialog.dismiss()
        }

        regionDialog.show()
    }


}