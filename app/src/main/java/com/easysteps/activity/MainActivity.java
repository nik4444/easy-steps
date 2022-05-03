package com.easysteps.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.easysteps.R;
import com.easysteps.fragment.DailyStepHistoryFragment;
import com.easysteps.fragment.DealFragment;
import com.easysteps.fragment.HomeFragment;
import com.easysteps.fragment.SettingsFragment;
import com.easysteps.helper.BaseActivity;
import com.easysteps.helper.PrefKey;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Locale;

public class MainActivity extends BaseActivity {

    private static final String PREFS_SORT_LANGUAGE_NAME = "SORT_LANGUAGE";
    public static BottomNavigationView bottom_navigation;
    public static CardView card_navigation;
    public static int steps_today;
    public static long steps;
    public int mFragment = 0;
    Animation scaleup, scaledown;
    boolean account_first_time = false;
    private FitnessOptions fitnessOptions;
    private GoogleSignInAccount account;
    private final int GOOGLE_FIT_PERMISSION_REQUEST_CODE = 1;
    private int myLiveSteps;
    private LinearLayout ll_google_sign_in;

    public static String getLanguage(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_SORT_LANGUAGE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, "en");
    }

    public void ChangeLanguage(String lan) {
        Resources res = getActivity().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lan.toLowerCase()));
        res.updateConfiguration(conf, dm);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChangeLanguage(getLanguage(this, PREFS_SORT_LANGUAGE_NAME));
        loadFragment(new HomeFragment());

        bottom_navigation = findViewById(R.id.bottomNavigation);
        card_navigation = findViewById(R.id.cvNavigation);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            try {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            } catch (Exception e) {
            }

        }

        bottom_navigation.setItemIconTintList(null);

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.nav_home) {
                    changeFragment(new HomeFragment(), HomeFragment.class
                            .getSimpleName());
                    return true;
                } else if (id == R.id.nav_time) {
                    changeFragment(new DailyStepHistoryFragment(), DailyStepHistoryFragment.class
                            .getSimpleName());
                    return true;
                } else if (id == R.id.nav_gift) {
                    changeFragment(new DealFragment(), DealFragment.class
                            .getSimpleName());
                    return true;
                } else if (id == R.id.nav_settings) {
                    changeFragment(new SettingsFragment(), SettingsFragment.class
                            .getSimpleName());
                    return true;
                }
                return true;
            }
        });
    }

    public void changeFragment(Fragment fragment, String tagFragmentName) {

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.replace(R.id.frameContainer, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSION_REQUEST_CODE) {
                Prefs.putBoolean(PrefKey.account_first_time, true);
                new Handler().postDelayed(() -> loadFragment(new HomeFragment()), 1500);
            }
        } else {
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission are Granted", Toast.LENGTH_SHORT).show();
                } else {
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    return;
                }
            }
        }
        super.onBackPressed();

    }
}