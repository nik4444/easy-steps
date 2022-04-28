package com.easysteps.helper;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    protected void showLoader() {
        Utils.ShowProgressDialog(this);
    }

    protected void hideLoader() {
        Utils.HideProgressDialog(this);
    }

    public BaseActivity getActivity() {
        return this;
    }

}
