package com.easysteps.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easysteps.R;
import com.easysteps.adapter.DealAdapter;
import com.easysteps.helper.Utils;
import com.easysteps.model.DealRes;
import com.easysteps.model.deal.GetDealRes;
import com.easysteps.retrofit.AsyncHttpRequest;
import com.easysteps.retrofit.AsyncResponseHandler;
import com.easysteps.retrofit.RequestParamsUtils;
import com.easysteps.retrofit.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.FormBody;


public class DealFragment extends Fragment {

    private static final String PREFS_SORT_LANGUAGE_NAME = "SORT_LANGUAGE";
    RecyclerView rv_deal;
    DealAdapter dealAdapter;
    ArrayList<DealRes> dealsArrayList = new ArrayList<>();
    TextView txt_coins;
    TextView txt_today_date;
    private String todayDate;


    public DealFragment() {
        // Required empty public constructor
    }

    public static DealFragment newInstance() {
        DealFragment fragment = new DealFragment();
        return fragment;
    }

    public static String getSortLanguage(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_SORT_LANGUAGE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, "en");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_deal = view.findViewById(R.id.rv_deal);
        txt_coins = view.findViewById(R.id.txt_coins);
        txt_today_date = view.findViewById(R.id.txt_today_date);

        String sortLanguage = getSortLanguage(getActivity(), PREFS_SORT_LANGUAGE_NAME);

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", new Locale(sortLanguage, sortLanguage.toUpperCase(Locale.ROOT)));
        todayDate = sdf.format(new Date());
        //byME
//        if (stepCoins == 0) {
//            stepCoins = 0;
//        }
//        txt_coins.setText("" + stepCoins);
        txt_today_date.setText("" + todayDate);
        GetDealsData();

        dealAdapter = new DealAdapter(getActivity(), dealsArrayList);
        rv_deal.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_deal.setAdapter(dealAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void GetDealsData() {
        try {
            Utils.ShowProgressDialog(getActivity());
            FormBody.Builder body = RequestParamsUtils.newRequestFormBody(getActivity());
            Call call = AsyncHttpRequest.newRequestPost(getActivity(), body.build(), URLs.GET_DEALS_DATA());
            call.enqueue(new GetDealsDataHandler(getActivity()));
            Log.e("TAG", "GetDealsData: " + Utils.getUserToken());
        } catch (Exception e) {
            Log.e(" Log", "UpdateProfile: " + e.getMessage());
        }

    }

    private class GetDealsDataHandler extends AsyncResponseHandler {
        GetDealsDataHandler(Activity activity) {
            super(activity);
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(String responce) {
            new Handler(Looper.getMainLooper()).post(() -> {

                Utils.HideProgressDialog(getActivity());

                if (responce != null && responce.length() > 0) {
                    GetDealRes getDealRes = new Gson().fromJson(responce, new TypeToken<GetDealRes>() {
                    }.getType());
                    if (getDealRes.getStatus() == 1) {
                        dealsArrayList.clear();
                        for (int i = 0; i < getDealRes.getData().size(); i++) {
                            DealRes deal = new DealRes(
                                    "" + getDealRes.getData().get(i).getDealPicture(),
                                    "" + getDealRes.getData().get(i).getDealStatus(),
                                    "" + getDealRes.getData().get(i).getDealTitle(),
                                    "" + getDealRes.getData().get(i).getDealpoints(),
                                    "" + getDealRes.getData().get(i).getIsRunning(),
                                    "" + getDealRes.getData().get(i).getDealLink(),
                                    "" + getDealRes.getData().get(i).getDealFileType());
                            dealsArrayList.add(deal);
                        }
                        dealAdapter = new DealAdapter(getActivity(), dealsArrayList);
                        rv_deal.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        rv_deal.setAdapter(dealAdapter);
                    }
                }
            });
        }

        @Override
        public void onFinish() {
            try {
                Utils.HideProgressDialog(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable e, String content) {
            Utils.HideProgressDialog(getActivity());
        }
    }

}