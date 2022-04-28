package com.easysteps.service.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesUtils {
    public static String DISTANCE_MEASUERE_IS_SET = "DISTANCE_MEASUERE_IS_SET";
    public static String DISTANCE_MEASURE_IM_KM_KEY = "DISTANCE_MEASURE_IM_KM_KEY";
    static String SET_UNIT_PREF = "unitPrefrence";
    static String DATE_PREF = "date_today";
    private String FILE_NAME = "share_date";
    private String isFace = "face";
    private Context context;

    public SharedPreferencesUtils(String str) {
        this.FILE_NAME = str;
    }

    public SharedPreferencesUtils(Context context2) {
        this.context = context2;
    }

    public void setParam(String str, Object obj) {
        String simpleName = obj.getClass().getSimpleName();
        SharedPreferences.Editor edit = this.context.getSharedPreferences(this.FILE_NAME, 0).edit();
        if ("String".equals(simpleName)) {
            edit.putString(str, obj.toString());
        } else if ("Integer".equals(simpleName)) {
            edit.putInt(str, ((Integer) obj).intValue());
        } else if ("Boolean".equals(simpleName)) {
            edit.putBoolean(str, ((Boolean) obj).booleanValue());
        } else if ("Float".equals(simpleName)) {
            edit.putFloat(str, ((Float) obj).floatValue());
        } else if ("Long".equals(simpleName)) {
            edit.putLong(str, ((Long) obj).longValue());
        }
        edit.apply();
    }

    public Object getParam(String str, Object obj) {
        String simpleName = obj.getClass().getSimpleName();
        try{
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(this.FILE_NAME, 0);
            if ("String".equals(simpleName)) {
                return sharedPreferences.getString(str, (String) obj);
            }
            if ("Integer".equals(simpleName)) {
                return Integer.valueOf(sharedPreferences.getInt(str, ((Integer) obj).intValue()));
            }
            if ("Boolean".equals(simpleName)) {
                return Boolean.valueOf(sharedPreferences.getBoolean(str, ((Boolean) obj).booleanValue()));
            }
            if ("Float".equals(simpleName)) {
                return Float.valueOf(sharedPreferences.getFloat(str, ((Float) obj).floatValue()));
            }
            if ("Long".equals(simpleName)) {
                return Long.valueOf(sharedPreferences.getLong(str, ((Long) obj).longValue()));
            }
        }catch (NullPointerException exception){
            Log.e("TAG", "getParam: "+exception.toString() );
        }

        return null;
    }

}
