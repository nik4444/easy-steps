package com.easysteps.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.easysteps.R
import com.easysteps.activity.ExoPlayerActivity
import com.easysteps.adapter.DealsAdapter
import com.easysteps.base.BaseFragment
import com.easysteps.databinding.FragmentDealBinding
import com.easysteps.pref.SharedPref.language
import com.easysteps.pref.SharedPref.todayDate
import com.easysteps.pref.SharedPref.userCoins
import com.easysteps.viewModel.GetDealsViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by NIKUNJ on 09-05-2022.
 */
class DealFragment : BaseFragment<FragmentDealBinding>(R.layout.fragment_deal), View.OnClickListener {

    var dealsAdapter: DealsAdapter? = null

    val viewModel: GetDealsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpObserver()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.txtCoins.text = userCoins.toString()
        binding.txtTodayDate.text = todayDate
    }

    private fun setUpObserver() {
        viewModel.apiErrors.observe(requireActivity()) { handleError(it) }

        viewModel.appLoader.observe(requireActivity()) { updateLoaderUI(it) }

        viewModel.dataError.observe(requireActivity()) { it.printStackTrace() }

        viewModel.getDealsData.observe(requireActivity()) {
            if (it.status == 1) {
                it.data?.let { it1 -> dealsAdapter?.addAll(it1) }
            }
        }
    }

    private fun initView() {
        dealsAdapter = DealsAdapter()
        binding.rvDeal.adapter = dealsAdapter

        val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale(language, language.uppercase(Locale.ROOT)))
        val todayDate = sdf.format(Date())
        binding.txtTodayDate.text = todayDate
        viewModel.getDeals()

        dealsAdapter?.setItemClickListener { _, _, getDealData ->
            if (getDealData.DealLink == "3") {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getDealData.DealLink))
                requireActivity().startActivity(browserIntent)
            } else {
                val intent = Intent(context, ExoPlayerActivity::class.java)
                intent.putExtra("deal_link", getDealData.DealLink)
                intent.putExtra("deal_file_type", getDealData.DealLink)
                requireActivity().startActivity(intent)
            }
        }

    }

    override fun onClick(p0: View?) {

    }

}