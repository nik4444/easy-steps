package com.easysteps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easysteps.LoginActivity;
import com.easysteps.R;
import com.easysteps.helper.BaseActivity;
import com.easysteps.helper.PrefKey;
import com.easysteps.helper.Utils;
import com.easysteps.model.loginsignup.SignUpRes;
import com.easysteps.retrofit.AsyncHttpRequest;
import com.easysteps.retrofit.AsyncResponseHandler;
import com.easysteps.retrofit.RequestParamsUtils;
import com.easysteps.retrofit.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixplicity.easyprefs.library.Prefs;

import okhttp3.Call;
import okhttp3.MultipartBody;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    EditText edt_email;
    EditText edt_full_name;
    EditText edt_password;
    EditText edt_phone;
    TextView txt_register;
    TextView ll_login;
    LinearLayout ll_register;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_full_name = findViewById(R.id.edt_full_name);
        edt_email = findViewById(R.id.edt_email);
        edt_phone = findViewById(R.id.edt_phone);
        edt_password = findViewById(R.id.edt_password);
        txt_register = findViewById(R.id.txt_register);
        ll_login = findViewById(R.id.ll_login);
        ll_register = findViewById(R.id.ll_register);

        txt_register.setOnClickListener(this);
        ll_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_register:
                if (edt_full_name.getText().toString().isEmpty()) {
                    Utils.MyShortSnackbar(ll_register, getActivity().getResources().getString(R.string.text_empty_username));
                } else if (edt_email.getText().toString().isEmpty()) {
                    Utils.MyShortSnackbar(ll_register, getActivity().getResources().getString(R.string.text_empty_email));
                } else if (edt_phone.getText().toString().isEmpty()) {
                    Utils.MyShortSnackbar(ll_register, getActivity().getResources().getString(R.string.text_empty_mobile));
                } else if (edt_password.getText().toString().isEmpty()) {
                    Utils.MyShortSnackbar(ll_register, getActivity().getResources().getString(R.string.text_empty_password));
                } else if (!Utils.isValidEmail(edt_email.getText().toString().trim())) {
                    Utils.MyShortSnackbar(ll_register, getActivity().getResources().getString(R.string.text_valid_email));
                } else if (!Utils.isValidPassword(edt_password.getText().toString().trim())) {
                    Utils.MyShortSnackbar(ll_register, getActivity().getResources().getString(R.string.text_valid_password));
                } else {
                    Prefs.putString(PrefKey.register_name, edt_full_name.getText().toString());
                    SignUp();
                }
                break;
            case R.id.ll_login:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

    private void SignUp() {
        try {
            Utils.ShowProgressDialog(getActivity());
            MultipartBody.Builder body = RequestParamsUtils.newRequestMultipartBody(this);
            body.setType(MultipartBody.FORM);
            body.addFormDataPart(RequestParamsUtils.userName, edt_full_name.getText().toString().trim());
            body.addFormDataPart(RequestParamsUtils.userEmail, edt_email.getText().toString().trim());
            body.addFormDataPart(RequestParamsUtils.userPhone, edt_phone.getText().toString().trim());
            body.addFormDataPart(RequestParamsUtils.userPassword, edt_password.getText().toString().trim());

            body.addFormDataPart(RequestParamsUtils.userLatitude, "21.132564");
            body.addFormDataPart(RequestParamsUtils.userLongitude, "12.123456");


            body.addFormDataPart(RequestParamsUtils.userDeviceToken, Prefs.getString(PrefKey.token, "testToken"));
            body.addFormDataPart(RequestParamsUtils.userDevice, "1");
            body.addFormDataPart(RequestParamsUtils.userDeviceId, Utils.getDeviceId(getActivity()));

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), body.build(), URLs.REGISTER());
            call.enqueue(new singUpHandler(getActivity()));

        } catch (Exception e) {
            Utils.MyShortToast(getActivity(), e.getMessage());
        }

    }

    //    APi Call Handle
    private class singUpHandler extends AsyncResponseHandler {
        singUpHandler(Activity activity) {
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
                    SignUpRes signUpRes = new Gson().fromJson(responce, new TypeToken<SignUpRes>() {
                    }.getType());
                    if (signUpRes.getStatus() == 1) {
                        Prefs.putString(PrefKey.login_info, new Gson().toJson(signUpRes.getSignUpData()));
                        Utils.setLoginStatus(true);
                        Utils.setUserToken(signUpRes.getSignUpData().getUserToken());
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        finishAffinity();
                    } else {
                        Utils.MyShortSnackbar(ll_register, "" + signUpRes.getMessage());
//                        Utils.MyShortToast(getActivity(),signUpRes.getMessage());
                    }
                }
            });
        }

        @Override
        public void onFinish() {
            try {
                Utils.HideProgressDialog(getActivity());
            } catch (Exception e) {
//                Utils.MyShortToast(getActivity(),e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable e, String content) {
//            Utils.MyShortToast(getActivity(),e.getMessage());
            Utils.HideProgressDialog(getActivity());
        }
    }
}