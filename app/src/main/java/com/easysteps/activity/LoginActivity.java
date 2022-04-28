package com.easysteps.activity;

import static com.easysteps.fragment.SettingsFragment.IS_FACE_LOCK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.easysteps.MyApplication;
import com.easysteps.R;
import com.easysteps.helper.BaseActivity;
import com.easysteps.helper.PrefKey;
import com.easysteps.helper.Utils;
import com.easysteps.model.loginsignup.SignUpRes;
import com.easysteps.network.ApiResponse;
import com.easysteps.retrofit.AsyncResponseHandler;
import com.easysteps.retrofit.RequestParamsUtils;
import com.easysteps.viewmodel.LoginViewModel;
import com.easysteps.viewmodel.ViewModelFactory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.HashMap;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.HttpException;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public static final String IS_REMEMBER = "IS_REMEMBER";
    public static final String IS_REMEMBER_EMAIL = "IS_REMEMBER_EMAIL";
    public static final String IS_REMEMBER_PASSWORD = "IS_REMEMBER_PASSWORD";
    TextView txt_register;
    TextView txt_forgot_password;
    EditText edt_email;
    EditText edt_password;
    TextView txt_login;
    ImageView img_remember;
    boolean isRemember = false;
    boolean isFaceLocked = false;
    String email;
    String password;
    LinearLayout ll_login;

    @Inject
    ViewModelFactory viewModelFactory;
    LoginViewModel loginViewModel;

    public static boolean getSwitchValue(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(IS_FACE_LOCK, Context.MODE_PRIVATE);
        return settings.getBoolean(key, false);
    }

    public static boolean getRememberValue(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(IS_REMEMBER, Context.MODE_PRIVATE);
        return settings.getBoolean(key, false);
    }

    public static void setRememberValue(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(IS_REMEMBER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void setRememberEmail(Context context, String key, String str) {
        SharedPreferences settings = context.getSharedPreferences(IS_REMEMBER_EMAIL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, str);
        editor.apply();
    }

    public static String getRememberEmail(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(IS_REMEMBER_EMAIL, Context.MODE_PRIVATE);
        return settings.getString(key, "");
    }

    public static void setRememberPassword(Context context, String key, String str) {
        SharedPreferences settings = context.getSharedPreferences(IS_REMEMBER_PASSWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, str);
        editor.apply();
    }

    public static String getRememberPassword(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(IS_REMEMBER_PASSWORD, Context.MODE_PRIVATE);
        return settings.getString(key, "");
    }

    public static void ClearEmail(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(IS_REMEMBER_EMAIL, Context.MODE_PRIVATE);
        SharedPreferences preferences2 = context.getSharedPreferences(IS_REMEMBER_PASSWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        SharedPreferences.Editor editor2 = preferences2.edit();
        editor.clear();
        editor2.clear();
        editor.apply();
        editor2.apply();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((MyApplication) getApplication()).getAppComponent().doInjection(this);

        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);

        loginViewModel.loginResponse().observe(this, this::consumeResponse);

        isFaceLocked = getSwitchValue(this, IS_FACE_LOCK);
        isRemember = getRememberValue(this, IS_REMEMBER);
        email = getRememberEmail(this, IS_REMEMBER_EMAIL);
        password = getRememberPassword(this, IS_REMEMBER_PASSWORD);

        Executor executor = ContextCompat.getMainExecutor(this);

        Log.e("TAG", " onCreate: " + Prefs.getString(PrefKey.userToken, "test"));

        BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.e("TAG", "onAuthenticationError: " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Log in using fingerprint or face")
                .setNegativeButtonText("Cancel")
                .build();

        initViewOrClick();

        if (isRemember) {
            img_remember.setImageResource(R.drawable.ic_checked);
            edt_email.setText(email);
            edt_password.setText(password);
        }

        if (isFaceLocked) {
            biometricPrompt.authenticate(promptInfo);
            Log.e("TAG", "isFaceLocked: " + isFaceLocked);
        }

    }

    private void initViewOrClick() {
        txt_register = findViewById(R.id.txt_register);
        txt_forgot_password = findViewById(R.id.txt_forgot_password);

        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        img_remember = (ImageView) findViewById(R.id.img_remember);
        ll_login = (LinearLayout) findViewById(R.id.ll_login);

        txt_login = findViewById(R.id.txt_login);
        txt_register.setOnClickListener(this);
        txt_forgot_password.setOnClickListener(this);
        txt_login.setOnClickListener(this);
        img_remember.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_remember) {
            if (isRemember) {
                isRemember = false;
                setRememberValue(this, IS_REMEMBER, false);
                ClearEmail(this);
                img_remember.setImageResource(R.drawable.ic_check);
            } else {
                isRemember = true;
                setRememberValue(this, IS_REMEMBER, true);
                img_remember.setImageResource(R.drawable.ic_checked);
                setRememberEmail(this, IS_REMEMBER_EMAIL, edt_email.getText().toString().trim());
                setRememberPassword(this, IS_REMEMBER_PASSWORD, edt_password.getText().toString().trim());
            }
        } else if (v.getId() == R.id.txt_register) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        } else if (v.getId() == R.id.txt_forgot_password) {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        } else if (v.getId() == R.id.txt_login) {
            if (edt_email.getText().toString().isEmpty()) {
                Utils.MyShortToast(getActivity(), getActivity().getResources().getString(R.string.text_empty_email));
            } else if (edt_password.getText().toString().isEmpty()) {
                Utils.MyShortSnackbar(ll_login, getActivity().getResources().getString(R.string.text_empty_password));
            } else if (!Utils.isValidEmail(edt_email.getText().toString().trim())) {
                Utils.MyShortSnackbar(ll_login, getActivity().getResources().getString(R.string.text_valid_email));
            } else if (!Utils.isValidPassword(edt_password.getText().toString().trim())) {
                Utils.MyShortSnackbar(ll_login, getActivity().getResources().getString(R.string.text_valid_password));
            } else {
                SignIn();
            }
        }
    }

    private void SignIn() {
        HashMap<String, String> map = new HashMap<>();
        map.put(RequestParamsUtils.userEmail, edt_email.getText().toString().trim());
        map.put(RequestParamsUtils.userPassword, edt_password.getText().toString().trim());
        map.put(RequestParamsUtils.userLatitude, "21.132564");
        map.put(RequestParamsUtils.userLongitude, "12.123456");
        map.put(RequestParamsUtils.userDeviceToken, Prefs.getString(PrefKey.token, "testToken"));
        map.put(RequestParamsUtils.userDevice, "1");
        map.put(RequestParamsUtils.userDeviceId, Utils.getDeviceId(getActivity()));

        loginViewModel.getLogin(this, map);

//        try {
//            Utils.ShowProgressDialog(getActivity());
//            MultipartBody.Builder body = RequestParamsUtils.newRequestMultipartBody(this);
//            body.setType(MultipartBody.FORM);
//            body.addFormDataPart(RequestParamsUtils.userEmail, edt_email.getText().toString().trim());
//            body.addFormDataPart(RequestParamsUtils.userPassword, edt_password.getText().toString().trim());
//
//            body.addFormDataPart(RequestParamsUtils.userLatitude, "21.132564");
//            body.addFormDataPart(RequestParamsUtils.userLongitude, "12.123456");
//            body.addFormDataPart(RequestParamsUtils.userDeviceToken, Prefs.getString(PrefKey.token, "testToken"));
//            body.addFormDataPart(RequestParamsUtils.userDevice, "1");
//            body.addFormDataPart(RequestParamsUtils.userDeviceId, Utils.getDeviceId(getActivity()));
//
//            Call call = AsyncHttpRequest.newRequestPost(getActivity(), body.build(), URLs.LOGIN());
//            call.enqueue(new singInHandler(getActivity()));
//        } catch (Exception e) {
//            Log.d("response=", e.toString());
//        }

    }

    private void consumeResponse(ApiResponse apiResponse) {

        switch (apiResponse.status) {

            case LOADING:
                showLoader();
                break;

            case SUCCESS:
                hideLoader();
                assert apiResponse.data != null;
                renderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                hideLoader();
                if (apiResponse.error instanceof HttpException) {
                    HttpException exception = (HttpException) apiResponse.error;
                    switch (exception.code()) {
                        case 400:
                            Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(this, "internal server", Toast.LENGTH_SHORT).show();
                            break;
                        case 408:
                            Toast.makeText(this, "time out", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                break;

            default:
                break;
        }
    }

    private void renderSuccessResponse(JsonElement response) {
        if (!response.isJsonNull()) {
            Gson gson = new Gson();
//            Networks applicableListData = gson.fromJson(response, Networks.class);
//
//            paymentRecyclerView.setAdapter(paymentMethodAdapter);
//            paymentMethodAdapter.setPaymentList(applicableListData.getNetworks().getApplicableListData());

            Log.d("response=>", response.toString());
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }

    private class singInHandler extends AsyncResponseHandler {
        singInHandler(Activity activity) {
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
                        Utils.MyShortSnackbar(ll_login, "" + signUpRes.getMessage());
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