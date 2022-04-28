package com.easysteps.retrofit;

import android.app.Activity;
import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;


public abstract class AsyncResponseHandler implements Callback {

    private Activity context;

    public AsyncResponseHandler(Activity context) {
        this.context = context;
    }

    public AsyncResponseHandler(Context context) {
        onStart();
    }

    abstract public void onStart();

    abstract public void onSuccess(String content);

    abstract public void onFinish();

    abstract public void onFailure(Throwable e, String content);

    @Override
    public void onFailure(Call call, IOException e) {
        onFailure(e, "");
    }

    @Override
    public void onResponse(Call call, okhttp3.Response response) throws IOException {
        onFinish();
        if (response.code() == 401) {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(response.body().source().inputStream()));
            String read;
            while ((read = br.readLine()) != null) {
                sb.append(read);
            }
            br.close();

            try {
                if (!StringUtils.isEmpty(sb.toString())) {
                    onFailure(new Throwable(""), "" + sb.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }

        if (response.isSuccessful()) {

            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(response.body().source().inputStream()));
            String read;

            while ((read = br.readLine()) != null) {
                sb.append(read);
            }
            br.close();

            try {
                onSuccess(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(response.body().source().inputStream()));
            String read;

            while ((read = br.readLine()) != null) {
                sb.append(read);
            }
            br.close();
            onFailure(new Throwable(""), "" + sb.toString());
        }
    }
}