package com.easysteps;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.webkit.WebView;

import com.easysteps.helper.PrefKey;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pixplicity.easyprefs.library.Prefs;

public class MyApplication extends Application {
    private static final String PROCESS = "com.easysteps";

    public static Context getContext() {
        return getContext();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate() {
        super.onCreate();

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(this.getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        initPieWebView();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    Prefs.putString(PrefKey.token, token);

                    PrefKey.token = token;
                });
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    private void initPieWebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName(this);
            if (!PROCESS.equals(processName)) {
                WebView.setDataDirectorySuffix(getString(processName, "sunzn"));
            }
        }
    }

    public String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }

    public String getString(String s, String defValue) {
        return isEmpty(s) ? defValue : s;
    }

    public boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }


}