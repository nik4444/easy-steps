package com.easysteps.adapter

import com.easysteps.R
import com.easysteps.base.BaseAdapter
import com.easysteps.databinding.RawDailySetpHistoryBinding
import com.easysteps.viewModel.models.GetMyStepsHistoryData

class StepHistoryAdapter : BaseAdapter<RawDailySetpHistoryBinding, GetMyStepsHistoryData>(R.layout.raw_daily_setp_history) {

    override fun setClickableView(binding: RawDailySetpHistoryBinding) =
        listOf(binding.root)

    override fun onBind(
        binding: RawDailySetpHistoryBinding,
        position: Int,
        item: GetMyStepsHistoryData,
        payloads: MutableList<Any>?
    ) {
        binding.run {

            txtDate.text = item.FormatStepsDate
            txtMonth.text = item.FormatStepsMonth
            txtYear.text = item.FormatStepsYear

            val stepsCount = item.StepsCount.toString()

            if (stepsCount.length == 1) {
                txtStepCount.text = item.StepsCount.toString()
            }
            if (stepsCount.length == 2) {
                txtStepCount.text = item.StepsCount.toString()
            }
            if (stepsCount.length == 3) {
                txtStepCount.text = item.StepsCount.toString()
            }
            if (stepsCount.length == 4) {
                txtStepCount.text = insertString(stepsCount, 0)
            }
            if (stepsCount.length == 5) {
                txtStepCount.text = insertString(stepsCount, 1)
            }
            txtKm.text = item.StepsKm.toString()

            val historyCoins = item.UserCoins.toString()

            if (historyCoins.length == 1) {
                txtEarnedCoin.text = item.UserCoins.toString()
            }
            if (historyCoins.length == 2) {
                txtEarnedCoin.text = item.UserCoins.toString()
            }
            if (historyCoins.length == 3) {
                txtEarnedCoin.text = item.UserCoins.toString()
            }
            if (historyCoins.length == 4) {
                txtEarnedCoin.text = insertString(historyCoins, 0)
            }
            if (historyCoins.length == 5) {
                txtEarnedCoin.text = insertString(historyCoins, 1)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun insertString(originalString: String, index: Int): String? {
        var newString: String? = String()
        for (i in originalString.indices) {
            newString += originalString[i]
            if (i == index) {
                newString += " "
            }
        }
        return newString
    }
}