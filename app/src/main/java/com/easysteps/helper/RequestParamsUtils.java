package com.easysteps.helper;

import android.content.Context;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RequestParamsUtils {

    public static final String USER_TOKEN = "user_token";
    public static final String userName = "userName";
    public static final String userEmail = "userEmail";
    public static final String userOtp = "userOtp";
    public static final String userNewPass = "userNewPass";
    public static final String userPhone = "userPhone";
    public static final String userPassword = "userPassword";
    public static final String userLatitude = "userLatitude";
    public static final String userLongitude = "userLongitude";
    public static final String userDeviceToken = "userDeviceToken";
    public static final String userDevice = "userDevice";
    public static final String userDeviceId = "userDeviceId";
    public static final String userId = "id";
    public static final String user_Id = "user_id";
    public static final String device_id = "device_id";
    public static final String userAddress = "userAddress";
    public static final String userCity = "userCity";
    public static final String userPostCode = "userPostCode";
    public static final String userSteps = "userSteps";
    public static final String userCountry = "userCountry";
    public static final String userState = "userState";
    public static final String email_verified_at = "email_verified_at";
    public static final String userProfile = "userProfile";
    public static final String userBlocked = "userBlocked";
    public static final String created_at = "created_at";
    public static final String updated_at = "updated_at";
    public static final String userRegion = "userRegion";
    public static final String userLanguage = "userLanguage";
    public static final String RewardedId = "RewardedId";
    public static final String RewardedCoins = "RewardedCoins";
    public static final String StepsCount = "StepsCount";
    public static final String StepsKm = "StepsKm";
    public static final String StepsDate = "StepsDate";
    public static final String UserCoins = "UserCoins";
    public static final String CurrentPassword = "CurrentPassword";
    public static final String NewPassword = "NewPassword";
    public static final String ContactSubject = "ContactSubject";
    public static final String ContactTo = "ContactTo";
    static final String AUTHORIZATION = "Authorization";
    static final String ACCEPT = "Accept";

    public static RequestBody newRequestBody(Context context, String json) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        return body;
    }

    public static FormBody.Builder newRequestFormBody(Context c) {
        FormBody.Builder builder = new FormBody.Builder();

        return builder;
    }

    public static MultipartBody.Builder newRequestMultipartBody(Context c) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        return builder;
    }

    public static HttpUrl.Builder newRequestUrlBuilder(Context c, String url) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

        return urlBuilder;

    }
}
