package com.easysteps.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.easysteps.R;
import com.easysteps.helper.PrefKey;
import com.easysteps.helper.Utils;
import com.easysteps.model.updateprofile.UpdateProfileRes;
import com.easysteps.retrofit.AsyncHttpRequest;
import com.easysteps.retrofit.AsyncResponseHandler;
import com.easysteps.retrofit.RequestParamsUtils;
import com.easysteps.retrofit.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixplicity.easyprefs.library.Prefs;
import com.vikktorn.picker.City;
import com.vikktorn.picker.CityPicker;
import com.vikktorn.picker.Country;
import com.vikktorn.picker.CountryPicker;
import com.vikktorn.picker.OnCountryPickerListener;
import com.vikktorn.picker.State;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;


public class ProfileFragment extends Fragment implements View.OnClickListener, /*OnCityPickerListener,*/ OnCountryPickerListener/*, OnStatePickerListener*/ {

    public static List<City> cityObject;
    public static List<State> stateObject;
    public String country;
    TextView txt_country, txt_save;
    EditText edt_name, edt_email_address, edt_postcode, edt_address, edt_state, edt_city;
    String SelectedCountry;
    LinearLayout ic_back;
    private CityPicker cityPicker;
    private CountryPicker countryPicker;
    private int countryID;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//ByME
//        if (card_navigation.getVisibility() == View.VISIBLE) {
//            card_navigation.setVisibility(View.GONE);
//        }
        initViewOrClick(view);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //BYME
//        if (card_navigation.getVisibility() == View.GONE) {
//            card_navigation.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViewOrClick(View view) {
        stateObject = new ArrayList<>();
        cityObject = new ArrayList<>();
        try {
            getStateJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            getCityJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        countryPicker = new CountryPicker.Builder().with(getActivity()).listener(this).build();
        initView(view);
        setData();
    }

    private void initView(View view) {
        edt_name = view.findViewById(R.id.edt_name);
        edt_email_address = view.findViewById(R.id.edt_email_address);
        edt_address = view.findViewById(R.id.edt_address);
        edt_postcode = view.findViewById(R.id.edt_postcode);
        edt_city = view.findViewById(R.id.edt_city);
        txt_country = view.findViewById(R.id.txt_country);
        edt_state = view.findViewById(R.id.edt_state);
        txt_save = view.findViewById(R.id.txt_save);
        ic_back = view.findViewById(R.id.ic_back);
        txt_country.setOnClickListener(this);
        txt_save.setOnClickListener(this);
        ic_back.setOnClickListener(this);
    }
    private void setData() {
        edt_name.setText(Utils.getLoginDataData().getUserName());
        edt_address.setText(Utils.getLoginDataData().getUserAddress());
        txt_country.setText(Utils.getLoginDataData().getUserCountry());
        edt_state.setText(Utils.getLoginDataData().getUserState());
        edt_city.setText(Utils.getLoginDataData().getUserCity());
        edt_postcode.setText(Utils.getLoginDataData().getUserPostCode());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_back:
                FragmentManager manager = getFragmentManager();
                manager.popBackStackImmediate();
                break;
            case R.id.txt_country:
                countryPicker.showDialog(getActivity().getSupportFragmentManager());
                break;
            case R.id.txt_save:
                UpdateProfile();
                break;
        }
    }

    public void getCityJson() throws JSONException {
        String json = null;
        try {
            InputStream inputStream = getActivity().getAssets().open("cities.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONObject jsonObject = new JSONObject(json);
        JSONArray events = jsonObject.getJSONArray("cities");
        for (int j = 0; j < events.length(); j++) {
            JSONObject cit = events.getJSONObject(j);
            City cityData = new City();

            cityData.setCityId(Integer.parseInt(cit.getString("id")));
            cityData.setCityName(cit.getString("name"));
            cityData.setStateId(Integer.parseInt(cit.getString("state_id")));
            cityObject.add(cityData);
        }
    }

    @Override
    public void onSelectCountry(Country country) {
        SelectedCountry = country.getName();
        txt_country.setText("" + country.getName());
    }

    public void getStateJson() throws JSONException {
        String json = null;
        try {
            InputStream inputStream = getActivity().getAssets().open("states.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject(json);
        JSONArray events = jsonObject.getJSONArray("states");
        for (int j = 0; j < events.length(); j++) {
            JSONObject cit = events.getJSONObject(j);
            State stateData = new State();

            stateData.setStateId(Integer.parseInt(cit.getString("id")));
            stateData.setStateName(cit.getString("name"));
            stateData.setCountryId(Integer.parseInt(cit.getString("country_id")));
            stateObject.add(stateData);
        }
    }

    private void UpdateProfile() {
        try {
            Utils.ShowProgressDialog(getActivity());
            FormBody.Builder body = RequestParamsUtils.newRequestFormBody(getActivity());
            body.addEncoded(RequestParamsUtils.userAddress, edt_address.getText().toString());
            body.addEncoded(RequestParamsUtils.userCity, edt_city.getText().toString());
            body.addEncoded(RequestParamsUtils.userEmail, edt_email_address.getText().toString());
            body.addEncoded(RequestParamsUtils.userCountry, SelectedCountry == null ? "" : SelectedCountry);
            body.addEncoded(RequestParamsUtils.userState, edt_state.getText().toString());
            body.addEncoded(RequestParamsUtils.userPostCode, edt_postcode.getText().toString());

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), body.build(), URLs.UPDATE_PROFILE());
            call.enqueue(new UpdateProfileHandler(getActivity()));
        } catch (Exception e) {
            Log.e(" Log", "UpdateProfile: " + e.getMessage());
        }

    }

    private class UpdateProfileHandler extends AsyncResponseHandler {
        UpdateProfileHandler(Activity activity) {
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
                    UpdateProfileRes updateProfileRes = new Gson().fromJson(responce, new TypeToken<UpdateProfileRes>() {
                    }.getType());
                    if (updateProfileRes.getStatus() == 1) {
                        Prefs.putString(PrefKey.login_info, new Gson().toJson(updateProfileRes.getData()));
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