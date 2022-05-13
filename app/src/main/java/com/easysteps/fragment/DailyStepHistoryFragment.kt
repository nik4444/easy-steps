package com.easysteps.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.easysteps.R
import com.easysteps.adapter.StepHistoryAdapter
import com.easysteps.base.BaseFragment
import com.easysteps.databinding.FragmentDailyStepHistoryBinding
import com.easysteps.viewModel.StepsHistoryViewModel

/**
 * Created by NIKUNJ on 09-05-2022.
 */
class DailyStepHistoryFragment : BaseFragment<FragmentDailyStepHistoryBinding>(R.layout.fragment_daily_step_history) {

    val viewModel: StepsHistoryViewModel by viewModels()
    var stepHistoryAdapter: StepHistoryAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpObserver()

    }

    private fun setUpObserver() {
        viewModel.apiErrors.observe(requireActivity()) { handleError(it) }

        viewModel.appLoader.observe(requireActivity()) { updateLoaderUI(it) }

        viewModel.dataError.observe(requireActivity()) { it.printStackTrace() }

        viewModel.getStepsHistoryData.observe(requireActivity()) {
            if (it.status == 1) {
                it.data?.let { it1 -> stepHistoryAdapter?.addAll(it1) }
            }
        }
    }

    private fun initView() {
        stepHistoryAdapter = StepHistoryAdapter()
        binding.rvHistory.adapter = stepHistoryAdapter
        viewModel.getStepsHistory()

    }
}