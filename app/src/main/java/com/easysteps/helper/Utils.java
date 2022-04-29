package com.easysteps.helper;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.easysteps.R;
import com.easysteps.model.loginsignup.SignUpData;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixplicity.easyprefs.library.Prefs;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static AlertDialog.Builder dialogBuilder;
    public static AlertDialog pd;

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo networkInfo : info) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getDeviceId(Context c) {
        String aid;
        try {
            aid = Settings.Secure.getString(c.getContentResolver(), "android_id");

            if (aid == null) {
                aid = "No DeviceId";
            } else if (aid.length() <= 0) {
                aid = "No DeviceId";
            }

        } catch (Exception e) {
            Utils.sendExceptionReport(e);
            aid = "No DeviceId";
        }

        return aid;

    }

    public static void sendExceptionReport(Exception e) {
        e.printStackTrace();

        try {
            // Writer result = new StringWriter();
            // PrintWriter printWriter = new PrintWriter(result);
            // e.printStackTrace(printWriter);
            // String stacktrace = result.toString();
            // new CustomExceptionHandler(c, URLs.URL_STACKTRACE)
            // .sendToServer(stacktrace);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static void ShowProgressDialog(Activity activity) {
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutBuilder = activity.getLayoutInflater().inflate(R.layout.custom_progress, null);
        LottieAnimationView lav_actionBar = layoutBuilder.findViewById(R.id.lav_actionBar);
        dialogBuilder.setView(layoutBuilder);

        lav_actionBar.setAnimation(R.raw.apiloader);
        lav_actionBar.playAnimation();
        lav_actionBar.loop(true);

        pd = dialogBuilder.create();
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pd.show();
    }

    public static void HideProgressDialog(Activity activity) {
        if (pd != null) {
            pd.dismiss();
        }
    }

    public static Boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    public static Boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static void MyShortSnackbar(LinearLayout layout, String message) {
        try {
            Snackbar snackbar = Snackbar.make(layout, message.toString(), Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            TextView textView = snackbarView.findViewById(R.id.snackbar_text);
            textView.setMaxLines(5);
            snackbar.show();
        } catch (Exception e) {

        }
    }

    public static void MyShortToast(Context context, String message) {
        // Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
        try {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
            view.getBackground().setColorFilter(context.getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(context.getResources().getColor(R.color.white));

            toast.show();
        } catch (NullPointerException e) {

        } catch (Exception e) {
            Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
        }


    }

    public static void MyLongToast(Context context, String message) {
        //Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
        try {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
            view.getBackground().setColorFilter(context.getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(context.getResources().getColor(R.color.white));

            toast.show();
        } catch (Exception e) {
            Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
        }


    }

    public static SignUpData getLoginDataData() {
        String data = Prefs.getString(PrefKey.login_info, "");
        Gson gson = new Gson();

        Type type = new TypeToken<SignUpData>() {
        }.getType();
        return gson.fromJson(data, type);
    }

    public static boolean isLogin() {
        if (Prefs.getBoolean(PrefKey.is_login, false)) {
            return true;
        } else {
            return false;
        }
    }

    public static int getSteps() {
        return Prefs.getInt(PrefKey.steps, 0);
    }

    public static Float getKM() {
        return Prefs.getFloat(PrefKey.km, 0);
    }

    public static void setLoginStatus(boolean value) {
        Prefs.putBoolean(PrefKey.is_login, value);
    }

    public static boolean getCoinAddedStatus() {
        return Prefs.getBoolean(PrefKey.coins_added, false);
    }

    public static void setCoinAddedStatus(boolean value) {
        Prefs.putBoolean(PrefKey.coins_added, value);
    }

    public static String getUserToken() {
        return Prefs.getString(PrefKey.userToken, "");
    }

    public static void setUserToken(String token) {
        Prefs.putString(PrefKey.userToken, token);
    }
}