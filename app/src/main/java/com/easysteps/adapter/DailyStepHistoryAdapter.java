package com.easysteps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easysteps.R;
import com.easysteps.model.dailystepshistory.StepsHistoryRes;

import java.util.ArrayList;

public class DailyStepHistoryAdapter extends RecyclerView.Adapter<DailyStepHistoryAdapter.MVH> {
    Context context;
        ArrayList<StepsHistoryRes> stepHistoryArrayListData;
        String historyCoins;
        String stepsCount;

    public DailyStepHistoryAdapter(Context context, ArrayList<StepsHistoryRes> dailyStepHistoryArrayList) {
        this.context = context;
        this.stepHistoryArrayListData = dailyStepHistoryArrayList;
    }

    public static String insertString (String originalString, String stringToBeInserted, int index) {
        String newString = new String();
        for (int i = 0; i < originalString.length(); i++) {
            newString += originalString.charAt(i);
            if (i == index) {
                newString += stringToBeInserted;
            }
        }
        return newString;
    }

    @NonNull
    @Override
    public DailyStepHistoryAdapter.MVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.raw_daily_setp_history, parent, false);
        return new MVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyStepHistoryAdapter.MVH holder, int position) {
        StepsHistoryRes historyData = stepHistoryArrayListData.get(position);
        holder.txt_date.setText(historyData.getFormatStepsDate());
        holder.txt_month.setText(historyData.getFormatStepsMonth());
        holder.txt_year.setText(historyData.getFormatStepsYear());

        stepsCount = historyData.getStepsCount();

        if(stepsCount.length() == 1){
            holder.txt_step_count.setText(historyData.getStepsCount());
        }
        if(stepsCount.length() == 2){
            holder.txt_step_count.setText(historyData.getStepsCount());
        }
        if(stepsCount.length() == 3){
            holder.txt_step_count.setText(historyData.getStepsCount());
        }
        if (stepsCount.length() == 4){
            holder.txt_step_count.setText(insertString(stepsCount," ",0));
        }
        if(stepsCount.length() == 5){
            holder.txt_step_count.setText(insertString(stepsCount," ",1));
        }
        holder.txt_km.setText("" + historyData.getStepsKm());
        historyCoins = historyData.getUserCoins();

        if(historyCoins.length() == 1){
            holder.txt_earned_coin.setText(historyData.getUserCoins());
        }
        if(historyCoins.length() == 2){
            holder.txt_earned_coin.setText(historyData.getUserCoins());
        }
        if(historyCoins.length() == 3){
            holder.txt_earned_coin.setText(historyData.getUserCoins());
        }
        if (historyCoins.length() == 4){
            holder.txt_earned_coin.setText(insertString(historyCoins," ",0));
        }
        if(historyCoins.length() == 5){
            holder.txt_earned_coin.setText(insertString(historyCoins," ",1));

        }
    }

    @Override
    public int getItemCount() {
        return stepHistoryArrayListData.size();
    }

    public class MVH extends RecyclerView.ViewHolder {
        TextView txt_date;
        TextView txt_month;
        TextView txt_year;
        TextView txt_step_count;
        TextView txt_km;
        TextView txt_earned_coin;

        public MVH(@NonNull View itemView) {
            super(itemView);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_month = (TextView) itemView.findViewById(R.id.txt_month);
            txt_year = (TextView) itemView.findViewById(R.id.txt_year);
            txt_step_count = itemView.findViewById(R.id.txt_step_count);
            txt_km = itemView.findViewById(R.id.txt_km);
            txt_earned_coin = itemView.findViewById(R.id.txt_earned_coin);
        }
    }
}
