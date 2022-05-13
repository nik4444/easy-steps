package com.easysteps.service.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.easysteps.R;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UIHelper {
    public static void setTimeTillDayEnd(TextView textView, Context context) {
        Calendar instance = Calendar.getInstance();
        int i = instance.get(11);
        int i2 = instance.get(12);
        Matcher matcher = Pattern.compile("(\\d{2}):(\\d{2})").matcher("00:00");
        if (matcher.matches()) {
            int parseInt = Integer.parseInt(matcher.group(1));
            int parseInt2 = Integer.parseInt(matcher.group(2));
            if (parseInt >= 24 || parseInt2 >= 60) {
                throw new IllegalArgumentException("Invalid time format: 00:00");
            }
            int i3 = ((parseInt * 60) + parseInt2) - ((i * 60) + i2);
            if (i3 < 0) {
                i3 += 1440;
            }
            int i4 = i3 / 60;
            textView.setText(i4 + " " + context.getString(R.string.hours) + " " + (i3 - (i4 * 60)) + " " + context.getString(R.string.minutes));
            return;
        }
        throw new IllegalArgumentException("Invalid time format: 00:00");
    }

    public static String getFormattedStringWithSpaces(String str) {
        int i;
        StringBuilder sb = new StringBuilder(str);
        try {
            i = Integer.parseInt(str);
        } catch (Exception unused) {
            i = 10000;
        }
        if (i > 9999) {
            sb.insert(2, " ");
        } else if (i > 999) {
            sb.insert(1, " ");
        }
        return sb.toString();
    }

    public static String getDistanceFromSteps(long i, Context context) {
        try {
            boolean booleanValue = ((Boolean) new SharedPreferencesUtils(context).getParam(SharedPreferencesUtils.DISTANCE_MEASURE_IM_KM_KEY, false)).booleanValue();
            Log.e("Ch--)", "" + booleanValue);
            if (!((Boolean) new SharedPreferencesUtils(context).getParam(SharedPreferencesUtils.DISTANCE_MEASURE_IM_KM_KEY, false)).booleanValue()) {
                return getMilesFromSteps(i, context);
            }
        } catch (NullPointerException exception) {
            Log.e("TAG", "getDistanceFromSteps: " + exception.getMessage());
        }

        return getKMFromSteps(i, context);
    }

    public static String getKMFromSteps(long i, Context context) {
        StringBuilder sb = new StringBuilder();
        double d = (double) i;
        Double.isNaN(d);
        Double.isNaN(d);
        sb.append(String.format("%.1f", Double.valueOf(d * 7.6E-4d)));
        return sb.toString();
    }

    public static String getMilesFromSteps(long i, Context context) {
        StringBuilder sb = new StringBuilder();
        double d = (double) i;
        Double.isNaN(d);
        Double.isNaN(d);
        sb.append(String.format("%.1f", Double.valueOf(d * 4.712E-4d)));
        return sb.toString();
    }

    public static String getCaloriesFromSteps(int i) {
        double d = (double) i;
        Double.isNaN(d);
        Double.isNaN(d);
        return String.format("%.2f", Double.valueOf(d * 0.111d));
    }
}
