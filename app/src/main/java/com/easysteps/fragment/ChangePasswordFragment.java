package com.easysteps.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.easysteps.R;
import com.easysteps.helper.Utils;
import com.easysteps.model.loginsignup.ChangePasswordRes;
import com.easysteps.retrofit.AsyncHttpRequest;
import com.easysteps.retrofit.AsyncResponseHandler;
import com.easysteps.retrofit.RequestParamsUtils;
import com.easysteps.retrofit.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.Call;
import okhttp3.MultipartBody;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    private ImageView img_back;
    private TextView txt_save;
    private EditText edt_current_password;
    private EditText edt_new_password;
    private EditText edt_confirm_password;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //byME
//        if (card_navigation.getVisibility() == View.VISIBLE){
//            card_navigation.setVisibility(View.GONE);
//        }

         img_back = view.findViewById(R.id.img_back);
        txt_save = view.findViewById(R.id.txt_save);
        edt_current_password = view.findViewById(R.id.edt_current_password);
        edt_new_password = view.findViewById(R.id.edt_new_password);
        edt_confirm_password = view.findViewById(R.id.edt_confirm_password);

        img_back.setOnClickListener(this);
        txt_save.setOnClickListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //byme
//        if (card_navigation.getVisibility() == View.GONE){
//            card_navigation.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                FragmentManager manager = getFragmentManager();
                manager.popBackStackImmediate();
                break;
            case R.id.txt_save:
                if (edt_new_password.getText().toString().trim().equals(edt_confirm_password.getText().toString().trim())){
                ChangePassword();
                }else {
                    Toast.makeText(getActivity(), "PLease Enter Valid Password!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ChangePassword() {
        try {
            Utils.ShowProgressDialog(getActivity());
            MultipartBody.Builder body = RequestParamsUtils.newRequestMultipartBody(getActivity());
            body.setType(MultipartBody.FORM);
            body.addFormDataPart(RequestParamsUtils.CurrentPassword, edt_current_password.getText().toString().trim());
            body.addFormDataPart(RequestParamsUtils.NewPassword, edt_new_password.getText().toString().trim());

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), body.build(), URLs.CHNAGE_PASSWORD());
            call.enqueue(new ChangePasswordHandler(getActivity()));
        }catch (Exception e){
            Log.e("TAG", "ForgotPassword: "+e.getMessage() );
        }

    }

    private class ChangePasswordHandler extends AsyncResponseHandler {
        ChangePasswordHandler(Activity activity) {
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
                    ChangePasswordRes changePasswordRes = new Gson().fromJson(responce, new TypeToken<ChangePasswordRes>() {
                    }.getType());
                    if (changePasswordRes.getStatus()==1){
//                        Toast.makeText(getActivity(), ""+changePasswordRes.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onSuccess: "+changePasswordRes.getMessage() );

                    }else {
//                        Toast.makeText(getActivity(), ""+changePasswordRes.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onSuccess: "+changePasswordRes.getMessage() );
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
            Log.e("TAG", "onFailure: "+ e.getMessage() );
            Utils.HideProgressDialog(getActivity());
        }
    }
}