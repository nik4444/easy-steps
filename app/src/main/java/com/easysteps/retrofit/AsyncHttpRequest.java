package com.easysteps.retrofit;


import android.content.Context;

import com.easysteps.helper.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AsyncHttpRequest extends OkHttpClient {

    public static Call newRequestPost(Context context, RequestBody body, String url) {

        final OkHttpClient client = new Builder()
                .connectTimeout(60, TimeUnit.MINUTES)
                .writeTimeout(60, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.MINUTES)
                .build();


        Request.Builder request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .header(RequestParamsUtils.AUTHORIZATION, "Bearer " + Utils.getUserToken())
                .header(RequestParamsUtils.ACCEPT, "application/json")
                .url(url)
                .post(body);

        Call call = client.newCall(request.build());
        return call;
    }

}
