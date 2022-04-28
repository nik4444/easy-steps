package com.easysteps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.easysteps.R;
import com.easysteps.helper.Utils;
import com.easysteps.model.loginsignup.ForgotPasswordRes;
import com.easysteps.retrofit.AsyncHttpRequest;
import com.easysteps.retrofit.AsyncResponseHandler;
import com.easysteps.retrofit.RequestParamsUtils;
import com.easysteps.retrofit.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.MultipartBody;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edt_email;
    TextView txt_send;
    LinearLayout ll_forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edt_email = findViewById(R.id.edt_email);
        ll_forgot = findViewById(R.id.ll_forgot);

        txt_send = findViewById(R.id.txt_send);

        txt_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_send:
                if (!validEmail(edt_email.getText().toString())) {
                    Toast.makeText(this,"Enter valid e-mail!",Toast.LENGTH_LONG).show();
                }else {
                    ForgotPassword();
                }
                break;
        }
    }
    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void ForgotPassword() {
        try {
            Utils.ShowProgressDialog(this);
            MultipartBody.Builder body = RequestParamsUtils.newRequestMultipartBody(this);
            body.setType(MultipartBody.FORM);
            body.addFormDataPart(RequestParamsUtils.userEmail, edt_email.getText().toString().trim());

            Call call = AsyncHttpRequest.newRequestPost(this, body.build(), URLs.FORGOT_PASSWORD());
            call.enqueue(new forgotPasswordHandler(this));
        }catch (Exception e){
            Log.e("TAG", "ForgotPassword: "+e.getMessage() );
        }

    }

    private class forgotPasswordHandler extends AsyncResponseHandler {
        forgotPasswordHandler(Activity activity) {
            super(activity);
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(String responce) {
            new Handler(Looper.getMainLooper()).post(() -> {

                Utils.HideProgressDialog(ForgotPasswordActivity.this);

                if (responce != null && responce.length() > 0) {
                    ForgotPasswordRes forgotPasswordRes = new Gson().fromJson(responce, new TypeToken<ForgotPasswordRes>() {
                    }.getType());
                    if (forgotPasswordRes.getStatus()==1){
                        Intent intent = new Intent(ForgotPasswordActivity.this,OtpActivity.class);
                        intent.putExtra("email",edt_email.getText().toString().trim());
                        startActivity(intent);
                    }else {
                        Utils.MyShortSnackbar(ll_forgot,""+forgotPasswordRes.getMessage());
                    }
                }
            });
        }

        @Override
        public void onFinish() {
            try {
                Utils.HideProgressDialog(ForgotPasswordActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable e, String content) {
            Log.e("TAG", "onFailure: "+ e.getMessage() );
            Utils.HideProgressDialog(ForgotPasswordActivity.this);
        }
    }

}