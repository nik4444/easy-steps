package com.easysteps.adapter;

import static com.easysteps.retrofit.URLs.IMAGE_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.easysteps.R;
import com.easysteps.activity.ExoPlayerActivity;
import com.easysteps.model.DealRes;

import java.util.ArrayList;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.MVH> {
    Context context;
    ArrayList<DealRes> dealsArrayList;
    public DealAdapter(Context context, ArrayList<DealRes> dealsArrayList) {
        this.context = context;
        this.dealsArrayList = dealsArrayList;
    }

    @NonNull
    @Override
    public DealAdapter.MVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.raw_deal,parent,false);
        return new MVH(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DealAdapter.MVH holder, int position) {
        DealRes dealData = dealsArrayList.get(position);
        String video_link = dealData.getDeal_link();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_placeholder);

        Glide.with(context).setDefaultRequestOptions(requestOptions)
                .load(IMAGE_URL+dealData.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_deal);
        holder.txt_deal_name.setText(dealData.getDeal_name());

        if(dealData.getDeal_point().equals("0")){
            if (holder.ll_points.getVisibility() == View.VISIBLE){
                holder.ll_points.setVisibility(View.GONE);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(15, 25, 15, 0);
            holder.txt_new.setLayoutParams(params);
        }else {
            if (holder.ll_points.getVisibility() == View.GONE){
                holder.ll_points.setVisibility(View.VISIBLE);
            }
            holder.txt_coins.setText(dealData.getDeal_point() + "  " +context.getString(R.string.txt_points));
        }

        if (dealData.getAd_new().equals("0")){
            holder.txt_new.setText("");
            holder.txt_new.setBackground(context.getDrawable(R.drawable.round_corner_empty));
        }else {
            holder.txt_new.setText(context.getString(R.string.text_new_ad));
        }

        if (dealData.getIsRunning().equals("0")){
            holder.txt_left.setText("");
            holder.txt_left.setBackground(context.getDrawable(R.drawable.round_corner_empty));
        }else if (dealData.getIsRunning().equals("1")){
            holder.txt_left.setText(context.getString(R.string.text_few_left));
            holder.txt_left.setBackground(context.getDrawable(R.drawable.round_corner_ads_new ));
        }else {
            holder.txt_left.setText(context.getString(R.string.text_many_left));
            holder.txt_left.setBackground(context.getDrawable(R.drawable.round_corner_many_left ));
        }

        holder.rl_deal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dealData.getDeal_file_type().equals("3")){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dealData.getDeal_link()));
                    context.startActivity(browserIntent);
                }else {
                    Intent intent = new Intent(context, ExoPlayerActivity.class);
                    intent.putExtra("deal_link",video_link);
                    intent.putExtra("deal_file_type",dealData.getDeal_file_type());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dealsArrayList.size();
    }

    public class MVH extends RecyclerView.ViewHolder {
        ImageView img_deal;
        TextView txt_new;
        TextView txt_coins;
        TextView txt_left;
        TextView txt_deal_name;
        RelativeLayout rl_deal;
        CardView card_view;
        LinearLayout ll_points;
        public MVH(@NonNull View itemView) {
            super(itemView);
            img_deal = itemView.findViewById(R.id.img_deal);
            txt_new = itemView.findViewById(R.id.txt_new);
            txt_coins = itemView.findViewById(R.id.txt_coins);
            txt_left = itemView.findViewById(R.id.txt_left);
            txt_deal_name = itemView.findViewById(R.id.txt_deal_name);
            rl_deal = itemView.findViewById(R.id.rl_deal);
            card_view = itemView.findViewById(R.id.card_view);
            ll_points = (LinearLayout) itemView.findViewById(R.id.ll_points);
        }
    }
}
