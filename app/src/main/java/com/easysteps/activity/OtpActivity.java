package com.easysteps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.easysteps.R;
import com.easysteps.helper.Utils;
import com.easysteps.model.loginsignup.OtpRes;
import com.easysteps.retrofit.AsyncHttpRequest;
import com.easysteps.retrofit.AsyncResponseHandler;
import com.easysteps.retrofit.RequestParamsUtils;
import com.easysteps.retrofit.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.Call;
import okhttp3.MultipartBody;

public class OtpActivity extends AppCompatActivity {

    String email;
    private EditText edt_otp;
    private EditText edt_new_password;
    private EditText edt_confirm_password;
    private TextView txt_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");


        edt_otp = (EditText) findViewById(R.id.edt_otp);
        edt_new_password = (EditText) findViewById(R.id.edt_new_password);
        edt_confirm_password = (EditText) findViewById(R.id.edt_confirm_password);
        txt_send = (TextView) findViewById(R.id.txt_send);



        txt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_new_password.getText().toString().trim().equals(edt_confirm_password.getText().toString().trim())){
                RestPassword();
                }else {
                    Toast.makeText(OtpActivity.this, "Please Enter Valid Password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void RestPassword() {
        try {
            Utils.ShowProgressDialog(this);
            MultipartBody.Builder body = RequestParamsUtils.newRequestMultipartBody(this);
            body.setType(MultipartBody.FORM);
            body.addFormDataPart(RequestParamsUtils.userOtp, "121212");
            body.addFormDataPart(RequestParamsUtils.userNewPass, edt_confirm_password.getText().toString().trim());
            body.addFormDataPart(RequestParamsUtils.userEmail, email);

            Call call = AsyncHttpRequest.newRequestPost(this, body.build(), URLs.RESET_PASSWORD());
            call.enqueue(new RestPasswordHandler(this));
        }catch (Exception e){
            Log.e("TAG", "ForgotPassword: "+e.getMessage() );
        }

    }

    private class RestPasswordHandler extends AsyncResponseHandler {
        RestPasswordHandler(Activity activity) {
            super(activity);
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(String responce) {
            new Handler(Looper.getMainLooper()).post(() -> {

                Utils.HideProgressDialog(OtpActivity.this);

                if (responce != null && responce.length() > 0) {
                    OtpRes otpRes = new Gson().fromJson(responce, new TypeToken<OtpRes>() {
                    }.getType());
                    if (otpRes.getStatus()==1){
                        startActivity(new Intent(OtpActivity.this,LoginActivity.class));
                        finish();
                    }else {
//                        Utils.MyShortToast(OtpActivity.this,otpRes.getMessage());
                    }
                }
            });
        }

        @Override
        public void onFinish() {
            try {
                Utils.HideProgressDialog(OtpActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable e, String content) {
            Log.e("TAG", "onFailure: "+ e.getMessage() );
            Utils.HideProgressDialog(OtpActivity.this);
        }
    }

}