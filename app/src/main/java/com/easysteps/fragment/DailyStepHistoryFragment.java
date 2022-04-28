package com.easysteps.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easysteps.R;
import com.easysteps.adapter.DailyStepHistoryAdapter;
import com.easysteps.helper.Utils;
import com.easysteps.model.dailystepshistory.GetMyStepsHistoryRes;
import com.easysteps.model.dailystepshistory.StepsHistoryRes;
import com.easysteps.retrofit.AsyncHttpRequest;
import com.easysteps.retrofit.AsyncResponseHandler;
import com.easysteps.retrofit.RequestParamsUtils;
import com.easysteps.retrofit.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.FormBody;


public class DailyStepHistoryFragment extends Fragment {

    RecyclerView rv_history;
    DailyStepHistoryAdapter dailyStepHistoryAdapter;

        ArrayList<StepsHistoryRes> dailyStepHistoryArrayList = new ArrayList<>();
//    ArrayList<DailyStepHistory> dailyStepHistoryArrayList = new ArrayList<>();

    public DailyStepHistoryFragment() {
        // Required empty public constructor
    }

    public static DailyStepHistoryFragment newInstance() {
        return new DailyStepHistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_step_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv_history = view.findViewById(R.id.rv_history);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GetMyStepsHistoryData();
            }
        }, 100);

        rv_history.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        dailyStepHistoryAdapter = new DailyStepHistoryAdapter(getActivity(),dailyStepHistoryArrayList);
        rv_history.setAdapter(dailyStepHistoryAdapter);
        rv_history.setNestedScrollingEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    private void GetMyStepsHistoryData() {
        try {
            Utils.ShowProgressDialog(getActivity());
            FormBody.Builder body = RequestParamsUtils.newRequestFormBody(getActivity());
            Call call = AsyncHttpRequest.newRequestPost(getActivity(), body.build(), URLs.GET_MY_STEPS_HISTORY());
            call.enqueue(new GetMyStepsHistoryHandler(getActivity()));
        } catch (Exception e) {
            Log.e(" Log", "UpdateProfile: " + e.getMessage());
        }

    }

    private class GetMyStepsHistoryHandler extends AsyncResponseHandler {
        GetMyStepsHistoryHandler(Activity activity) {
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
                    GetMyStepsHistoryRes getMyStepsHistoryRes = new Gson().fromJson(responce, new TypeToken<GetMyStepsHistoryRes>() {
                    }.getType());
                    if (getMyStepsHistoryRes.getStatus() == 1) {
                        dailyStepHistoryArrayList.clear();
                        for (int i = 0; i < getMyStepsHistoryRes.getData().size(); i++) {
                            if(getMyStepsHistoryRes.getData().get(i).getFormatStepsMonth() != null/* && getMyStepsHistoryRes.getData().get(i).getStepsCount() != 0*/){
                                StepsHistoryRes stepsHistoryRes = new StepsHistoryRes(
                                        ""+getMyStepsHistoryRes.getData().get(i).getStepsCount(),
                                        ""+getMyStepsHistoryRes.getData().get(i).getStepsKm(),
                                        ""+getMyStepsHistoryRes.getData().get(i).getUserCoins(),
                                        ""+getMyStepsHistoryRes.getData().get(i).getFormatStepsDate(),
                                        ""+getMyStepsHistoryRes.getData().get(i).getFormatStepsMonth(),
                                        ""+getMyStepsHistoryRes.getData().get(i).getFormatStepsYear());
                                dailyStepHistoryArrayList.add(stepsHistoryRes);
                            }
//                            Collections.reverse(dailyStepHistoryArrayList);
                        }
                        rv_history.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        dailyStepHistoryAdapter = new DailyStepHistoryAdapter(getActivity(),dailyStepHistoryArrayList);
                        rv_history.setAdapter(dailyStepHistoryAdapter);
                        rv_history.setNestedScrollingEnabled(false);
//                        Utils.MyShortToast(getActivity(), getMyStepsHistoryRes.getMessage());
                    } else {
//                        Utils.MyShortToast(getActivity(), getMyStepsHistoryRes.getMessage());
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