package com.easysteps.adapter

import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.easysteps.R
import com.easysteps.base.BaseAdapter
import com.easysteps.databinding.RawDealBinding
import com.easysteps.retrofit.URLs
import com.easysteps.viewModel.models.GetDealData

class DealsAdapter : BaseAdapter<RawDealBinding, GetDealData>(R.layout.raw_deal) {

    override fun setClickableView(binding: RawDealBinding) =
        listOf(binding.root)

    override fun onBind(
        binding: RawDealBinding,
        position: Int,
        item: GetDealData,
        payloads: MutableList<Any>?
    ) {
        binding.run {
            txtDealName.text = item.DealTitle

            if (item.Dealpoints == 0) {
                if (llPoints.visibility == View.VISIBLE) {
                    llPoints.visibility = View.GONE
                }
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(15, 25, 15, 0)
                txtNew.layoutParams = params
            } else {
                if (llPoints.visibility == View.GONE) {
                    llPoints.visibility = View.VISIBLE
                }
                txtCoins.text = "${item.Dealpoints} ${mContext.getString(R.string.txt_points)}"
            }

            if (item.DealStatus == 0) {
                txtNew.text = ""
                txtNew.background = ContextCompat.getDrawable(mContext, R.drawable.round_corner_empty)
            } else {
                txtNew.text = mContext.getString(R.string.text_new_ad)
            }

            when (item.IsRunning) {
                0 -> {
                    txtLeft.text = ""
                    txtLeft.background = ContextCompat.getDrawable(mContext, R.drawable.round_corner_empty)
                }
                1 -> {
                    txtLeft.text = mContext.getString(R.string.text_few_left)
                    txtLeft.background = ContextCompat.getDrawable(mContext, R.drawable.round_corner_ads_new)
                }
                else -> {
                    txtLeft.text = mContext.getString(R.string.text_many_left)
                    txtLeft.background = ContextCompat.getDrawable(mContext, R.drawable.round_corner_many_left)
                }
            }

            Glide.with(mContext).load(URLs.IMAGE_URL + item.DealPicture).placeholder(R.drawable.ic_placeholder)
                .into(imgDeal)
        }
    }

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}