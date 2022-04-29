package com.easysteps.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.easysteps.LoginActivity;
import com.easysteps.R;
import com.easysteps.activity.RegisterActivity;
import com.easysteps.helper.PrefKey;
import com.easysteps.helper.Utils;
import com.easysteps.model.DeleteAccountRes;
import com.easysteps.model.logout.LogOutRes;
import com.easysteps.retrofit.AsyncHttpRequest;
import com.easysteps.retrofit.AsyncResponseHandler;
import com.easysteps.retrofit.RequestParamsUtils;
import com.easysteps.retrofit.URLs;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixplicity.easyprefs.library.Prefs;

import okhttp3.Call;
import okhttp3.FormBody;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    public static final String IS_FACE_LOCK = "IS_FACE_LOCK";
    public static final String IS_REMEMBER_EMAIL = "IS_REMEMBER_EMAIL";
    public static final String IS_REMEMBER_PASSWORD = "IS_REMEMBER_PASSWORD";
    public static final String IS_REMEMBER = "IS_REMEMBER";
    LinearLayout ll_profile;
    LinearLayout ll_language;
    LinearLayout ll_learn_easy_steps;
    LinearLayout ll_contact_us;
    LinearLayout ll_rate_app;
    LinearLayout ll_term_condition;
    LinearLayout ll_policy;
    LinearLayout ll_delete_account;
    LinearLayout ll_confirm_delete;
    LinearLayout ll_cancel_delete;
    LinearLayout ll_logout;
    LinearLayout ll_change_password;
    Switch face_switch;
    boolean isFaceLocked = false;
    private FitnessOptions fitnessOptions;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    public static boolean getSwitchValue(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(IS_FACE_LOCK, Context.MODE_PRIVATE);
        return settings.getBoolean(key, false);
    }

    public static boolean setSwitchValue(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(IS_FACE_LOCK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
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

    public static boolean setRememberValue(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(IS_REMEMBER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_profile = view.findViewById(R.id.ll_profile);
        ll_language = view.findViewById(R.id.ll_language);
        ll_contact_us = view.findViewById(R.id.ll_contact_us);
        ll_learn_easy_steps = view.findViewById(R.id.ll_learn_easy_steps);
        ll_rate_app = view.findViewById(R.id.ll_rate_app);
        ll_term_condition = view.findViewById(R.id.ll_term_condition);
        ll_policy = view.findViewById(R.id.ll_policy);
        ll_delete_account = view.findViewById(R.id.ll_delete_account);
        ll_logout = view.findViewById(R.id.ll_logout);
        ll_change_password = view.findViewById(R.id.ll_change_password);
        face_switch = view.findViewById(R.id.face_switch);

        isFaceLocked = getSwitchValue(getActivity(), IS_FACE_LOCK);

        face_switch.setChecked(isFaceLocked);

        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        ll_profile.setOnClickListener(this);
        ll_language.setOnClickListener(this);
        ll_contact_us.setOnClickListener(this);
        ll_learn_easy_steps.setOnClickListener(this);
        ll_term_condition.setOnClickListener(this);
        ll_policy.setOnClickListener(this);
        ll_change_password.setOnClickListener(this);
        ll_logout.setOnClickListener(this);
        ll_rate_app.setOnClickListener(this);
        ll_delete_account.setOnClickListener(this);

        face_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isFaceLocked) {
                setSwitchValue(getActivity(), IS_FACE_LOCK, isChecked);
            } else {
                setSwitchValue(getActivity(), IS_FACE_LOCK, isChecked);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_profile:
                Fragment profileFragment = new ProfileFragment();
                loadChildFragment(profileFragment);
                break;
            case R.id.ll_language:
                Fragment languageFragment = new ReginLanguageFragment();
                loadChildFragment(languageFragment);
                break;
            case R.id.ll_learn_easy_steps:
                break;
            case R.id.ll_contact_us:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + getActivity().getString(R.string.text_mail)));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.e("TAG", "Error: " + e.getMessage());
                }
                break;
            case R.id.ll_term_condition:
                Intent termIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://46.101.95.217/easysteps/terms-condition"));
                startActivity(termIntent);
                break;
            case R.id.ll_policy:
                Intent policyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://46.101.95.217/easysteps/privacy-policy"));
                startActivity(policyIntent);
                break;
            case R.id.ll_change_password:
                Fragment changePasswordFragment = new ChangePasswordFragment();
                loadChildFragment(changePasswordFragment);
                break;
            case R.id.ll_logout:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.dialog_logout)
                        .setMessage(R.string.dialog_logout_message)
                        .setCancelable(false)
                        .setNegativeButton(R.string.dialog_no, ((dialogInterface, i) -> {
                        }))
                        .setPositiveButton(R.string.dialog_yes, (dialog, which) -> {
                            Fitness.getRecordingClient(getActivity(), getGoogleAccount())
                                    .unsubscribe(DataType.TYPE_STEP_COUNT_DELTA)
                                    .addOnSuccessListener(aVoid -> {
                                        GoogleSignInOptions gso = new GoogleSignInOptions.
                                                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                                build();
                                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                                        googleSignInClient.signOut();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("TAG", "Not Disconnect: " + e.getMessage());
                                    });

                            GoogleSignInOptions gso = new GoogleSignInOptions.
                                    Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                    build();
                            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                            googleSignInClient.signOut();
                            ClearEmail(getActivity());
                            setRememberValue(getActivity(), IS_REMEMBER, false);
                            Prefs.remove(PrefKey.register_name);
                            Prefs.remove(PrefKey.address);
                            Prefs.remove(PrefKey.country);
                            Prefs.remove(PrefKey.state);
                            Prefs.remove(PrefKey.city);
                            Prefs.remove(PrefKey.current_steps);
                            Prefs.remove(PrefKey.account_first_time);
                            Prefs.remove(PrefKey.post_code);
                            LogOut();
                        }).show();
                break;
            case R.id.ll_rate_app:
                try {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName() + "");
                    Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goMarket);
                } catch (ActivityNotFoundException e) {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName() + "");
                    Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goMarket);
                }
                break;
            case R.id.ll_delete_account:
                Dialog deleteDialog = new Dialog(getActivity());
                deleteDialog.setContentView(R.layout.dialog_delete);
                deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                deleteDialog.show();

                ll_confirm_delete = deleteDialog.findViewById(R.id.ll_confirm_delete);
                ll_cancel_delete = deleteDialog.findViewById(R.id.ll_cancel_delete);

                ll_confirm_delete.setOnClickListener(v1 -> {
                    Fitness.getRecordingClient(getActivity(), getGoogleAccount())
                            .unsubscribe(DataType.TYPE_STEP_COUNT_DELTA)
                            .addOnSuccessListener(aVoid -> {
                                GoogleSignInOptions gso = new GoogleSignInOptions.
                                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                        build();
                                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                                googleSignInClient.signOut();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("TAG", "Not Disconnect: " + e.getMessage());
                            });

                    GoogleSignInOptions gso = new GoogleSignInOptions.
                            Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                            build();
                    GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                    googleSignInClient.signOut();
                    ClearEmail(getActivity());
                    setRememberValue(getActivity(), IS_REMEMBER, false);
                    Prefs.remove(PrefKey.register_name);
                    Prefs.remove(PrefKey.address);
                    Prefs.remove(PrefKey.country);
                    Prefs.remove(PrefKey.state);
                    Prefs.remove(PrefKey.city);
                    Prefs.remove(PrefKey.current_steps);
                    Prefs.remove(PrefKey.account_first_time);
                    Prefs.remove(PrefKey.post_code);
                    DeleteAccount();
                    deleteDialog.dismiss();

                });
                ll_cancel_delete.setOnClickListener(v12 -> deleteDialog.dismiss());
                break;

        }
    }

    private GoogleSignInAccount getGoogleAccount() {
        return GoogleSignIn.getAccountForExtension(getActivity(), fitnessOptions);
    }

    public void loadChildFragment(Fragment nextFragment) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.setting_frame_container, nextFragment)
                .addToBackStack(String.valueOf(nextFragment))
                .commit();
    }

    private void LogOut() {
        try {
            Utils.ShowProgressDialog(getActivity());
            FormBody.Builder body = RequestParamsUtils.newRequestFormBody(getActivity());
            body.addEncoded(RequestParamsUtils.user_Id, Utils.getLoginDataData().getUserId().toString());
            body.addEncoded(RequestParamsUtils.device_id, Utils.getLoginDataData().getUserDeviceId().toString());

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), body.build(), URLs.LOGOUT());
            call.enqueue(new LogOutHandler(getActivity()));
        } catch (Exception e) {
            Log.e(" Log", "UpdateProfile: " + e.getMessage());
        }

    }

    private void DeleteAccount() {
        try {
            Utils.ShowProgressDialog(getActivity());
            FormBody.Builder body = RequestParamsUtils.newRequestFormBody(getActivity());
            body.addEncoded(RequestParamsUtils.user_Id, Utils.getLoginDataData().getUserId().toString());
            body.addEncoded(RequestParamsUtils.device_id, Utils.getLoginDataData().getUserDeviceId().toString());

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), body.build(), URLs.DELETE_ACCOUNT());
            call.enqueue(new DeleteAccountHandler(getActivity()));
        } catch (Exception e) {
            Log.e(" Log", "UpdateProfile: " + e.getMessage());
        }

    }

    private class LogOutHandler extends AsyncResponseHandler {
        LogOutHandler(Activity activity) {
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
                    LogOutRes logOutRes = new Gson().fromJson(responce, new TypeToken<LogOutRes>() {
                    }.getType());
                    if (logOutRes.getStatus() == 1) {
                        Prefs.remove(PrefKey.userToken);
                        Prefs.remove(PrefKey.is_login);
                        setSwitchValue(getActivity(), IS_FACE_LOCK, false);
//                        Utils.MyShortToast(getActivity(), logOutRes.getMessage());
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    } else {
//                        Utils.MyShortToast(getActivity(), logOutRes.getMessage());
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

    private class DeleteAccountHandler extends AsyncResponseHandler {
        DeleteAccountHandler(Activity activity) {
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
                    DeleteAccountRes deleteAccountRes = new Gson().fromJson(responce, new TypeToken<DeleteAccountRes>() {
                    }.getType());
                    if (deleteAccountRes.getStatus() == 1) {
                        Prefs.remove(PrefKey.userToken);
                        Prefs.remove(PrefKey.is_login);
//                        Utils.MyShortToast(getActivity(), deleteAccountRes.getMessage());
                        startActivity(new Intent(getActivity(), RegisterActivity.class));
                        getActivity().finish();
                    } else {
//                        Utils.MyShortToast(getActivity(), deleteAccountRes.getMessage());
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