package com.easysteps.fragment;

import static com.easysteps.activity.MainActivity.card_navigation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.easysteps.R;
import com.easysteps.activity.MainActivity;
import com.easysteps.helper.Utils;
import com.easysteps.model.updateregionlang.UpdateRegionLangRes;
import com.easysteps.retrofit.AsyncHttpRequest;
import com.easysteps.retrofit.AsyncResponseHandler;
import com.easysteps.retrofit.RequestParamsUtils;
import com.easysteps.retrofit.URLs;
import com.easysteps.service.Utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vikktorn.picker.Country;
import com.vikktorn.picker.CountryPicker;
import com.vikktorn.picker.OnCountryPickerListener;

import java.util.Locale;

import okhttp3.Call;
import okhttp3.FormBody;

public class ReginLanguageFragment extends Fragment implements View.OnClickListener, OnCountryPickerListener {

    private static final String PREFS_REGION_NAME = "REGION";
    private static final String PREFS_LANGUAGE_NAME = "LANGUAGE";
    private static final String PREFS_SORT_LANGUAGE_NAME = "SORT_LANGUAGE";
    private static final String PREFS_REGION_KEY = "region";
    private static final String PREFS_LANGUAGE_KEY = "region";
    SharedPreferencesUtils preferencesUtils;
    String region;
    String language;
    ImageView ic_back;
    private TextView txt_country;
    private TextView txt_language;
    private CountryPicker countryPicker;
    private LinearLayout ll_region;
    private LinearLayout ll_usa;
    private LinearLayout ll_uk;
    private LinearLayout ll_brazilian;
    private LinearLayout ll_argentina;
    private LinearLayout ll_uae;
    private LinearLayout ll_language;
    private LinearLayout ll_arabic;
    private LinearLayout ll_english;
    private LinearLayout ll_portuguese;
    private LinearLayout ll_spanish;
    private TextView txt_title;

    public ReginLanguageFragment() {
        // Required empty public constructor
    }

    public static ReginLanguageFragment newInstance(String param1, String param2) {
        ReginLanguageFragment fragment = new ReginLanguageFragment();
        return fragment;
    }

    public static boolean setRegion(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_REGION_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getRegion(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_REGION_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, "Not Selected");
    }

    public static boolean setSortLanguage(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_SORT_LANGUAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static boolean setLanguage(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_LANGUAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getLanguage(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_LANGUAGE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, "Not Selected");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_regin_language, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (card_navigation.getVisibility() == View.VISIBLE){
            card_navigation.setVisibility(View.GONE);
        }
        initView(view);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (card_navigation.getVisibility() == View.GONE){
            card_navigation.setVisibility(View.VISIBLE);
        }
    }

    private void initView(View view) {
        txt_country = (TextView) view.findViewById(R.id.txt_country);
        txt_language = (TextView) view.findViewById(R.id.txt_language);
        ll_region = (LinearLayout) view.findViewById(R.id.ll_region);
        ll_language = (LinearLayout) view.findViewById(R.id.ll_language);
        ic_back = (ImageView) view.findViewById(R.id.ic_back);
        ll_region.setOnClickListener(this);
        ll_language.setOnClickListener(this);
        ic_back.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        region = getRegion(getActivity(),PREFS_REGION_KEY);
        language = getLanguage(getActivity(),PREFS_LANGUAGE_KEY);
        txt_country.setText(region);
        txt_language.setText(language);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_region:
                Dialog regionDialog = new Dialog(getActivity(), android.R.style.Theme_Dialog);
                regionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                regionDialog.setContentView(R.layout.region_dialog);

                ll_usa = (LinearLayout) regionDialog.findViewById(R.id.ll_usa);
                ll_uk = (LinearLayout) regionDialog.findViewById(R.id.ll_uk);
                ll_brazilian = (LinearLayout) regionDialog.findViewById(R.id.ll_brazilian);
                ll_argentina = (LinearLayout) regionDialog.findViewById(R.id.ll_argentina);
                ll_uae = (LinearLayout) regionDialog.findViewById(R.id.ll_uae);

                ll_usa.setOnClickListener(view1 -> {
                    region = getActivity().getString(R.string.usa_language);
                    txt_country.setText(region);
                    UpdateRegionLang();
                    setRegion(getActivity(),PREFS_REGION_KEY,region);
                    regionDialog.dismiss();
                });
                ll_uk.setOnClickListener(view1 -> {
                    region = getActivity().getString(R.string.uk_language);
                    txt_country.setText(region);
                    UpdateRegionLang();
                    setRegion(getActivity(),PREFS_REGION_KEY,region);
                    regionDialog.dismiss();
                });
                ll_brazilian.setOnClickListener(view1 -> {
                    region = getActivity().getString(R.string.brazilian_language);
                    txt_country.setText(region);
                    UpdateRegionLang();
                    setRegion(getActivity(),PREFS_REGION_KEY,region);
                    regionDialog.dismiss();
                });
                ll_argentina.setOnClickListener(view1 -> {
                    region = getActivity().getString(R.string.argentina_language);
                    txt_country.setText(region);
                    UpdateRegionLang();
                    setRegion(getActivity(),PREFS_REGION_KEY,region);
                    regionDialog.dismiss();
                });
                ll_uae.setOnClickListener(view1 -> {
                    region = getActivity().getString(R.string.uae_language);
                    txt_country.setText(region);
                    UpdateRegionLang();
                    setRegion(getActivity(),PREFS_REGION_KEY,region);
                    regionDialog.dismiss();
                });

                regionDialog.show();
                break;
            case R.id.ic_back:
                FragmentManager manager = getFragmentManager();
                manager.popBackStackImmediate();
                break;
            case R.id.ll_language:
                Dialog languageDialog = new Dialog(getActivity(), android.R.style.Theme_Dialog);
                languageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                languageDialog.setContentView(R.layout.language_dialog);

                txt_title = (TextView) languageDialog.findViewById(R.id.txt_title);
                ll_arabic = (LinearLayout) languageDialog.findViewById(R.id.ll_arabic);
                ll_english = (LinearLayout) languageDialog.findViewById(R.id.ll_english);
                ll_portuguese = (LinearLayout) languageDialog.findViewById(R.id.ll_portuguese);
                ll_spanish = (LinearLayout) languageDialog.findViewById(R.id.ll_spanish);

                ll_arabic.setOnClickListener(view13 -> {
                    language = getActivity().getString(R.string.arabic_language);
                    txt_language.setText(language);
                    setLanguage(getActivity(),PREFS_LANGUAGE_KEY,language);
                    UpdateRegionLang();
                    ChangeLanguage("ar");
                    languageDialog.dismiss();
                });

                ll_english.setOnClickListener(view12 -> {
                    language = getActivity().getString(R.string.english_language);
                    txt_language.setText(language);
                    setLanguage(getActivity(),PREFS_LANGUAGE_KEY,language);
                    UpdateRegionLang();
                    ChangeLanguage("en");
                    languageDialog.dismiss();
                });

                ll_portuguese.setOnClickListener(view14 -> {
                    language = getActivity().getString(R.string.portuguese_language);
                    txt_language.setText(language);
                    setLanguage(getActivity(),PREFS_LANGUAGE_KEY,language);
                    UpdateRegionLang();
                    ChangeLanguage("pt");
                    languageDialog.dismiss();
                });

                ll_spanish.setOnClickListener(view15 -> {
                    language = getActivity().getString(R.string.spanish_language);
                    txt_language.setText(language);
                    setLanguage(getActivity(),PREFS_LANGUAGE_KEY,language);
                    UpdateRegionLang();
                    ChangeLanguage("es");
                    languageDialog.dismiss();
                });

                languageDialog.show();
                break;
        }
    }

    public void ChangeLanguage(String lan){
        Resources res = getActivity().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lan.toLowerCase()));
        res.updateConfiguration(conf, dm);
        setSortLanguage(getActivity(),PREFS_SORT_LANGUAGE_NAME,lan);
        Intent intent = new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onSelectCountry(Country country) {
        txt_country.setText(""+country.getName());
    }

    private void UpdateRegionLang() {
        try {
            Utils.ShowProgressDialog(getActivity());
            FormBody.Builder body = RequestParamsUtils.newRequestFormBody(getActivity());
            body.addEncoded(RequestParamsUtils.userRegion, region);
            body.addEncoded(RequestParamsUtils.userLanguage, language);

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), body.build(), URLs.UPDATE_REGIONLAG());
            call.enqueue(new UpdateRegionLangHandler(getActivity()));
        } catch (Exception e) {
            Log.e(" Log", "UpdateProfile: " + e.getMessage());
        }

    }

    private class UpdateRegionLangHandler extends AsyncResponseHandler {
        UpdateRegionLangHandler(Activity activity) {
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
                    UpdateRegionLangRes updateRegionLangRes = new Gson().fromJson(responce, new TypeToken<UpdateRegionLangRes>() {
                    }.getType());
                    if (updateRegionLangRes.getStatus() == 1) {
                    } else {
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