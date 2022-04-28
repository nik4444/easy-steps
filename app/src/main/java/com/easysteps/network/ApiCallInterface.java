package com.easysteps.network;

import com.google.gson.JsonElement;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by NIKUNJ
 *
 */
public interface ApiCallInterface {

    @GET(EndPoint.PAYMENT_METHODS)
    Observable<JsonElement> getPaymentMethods();

    @POST("signIn")
    Observable<JsonElement> login(@Body HashMap<String, String> body);

}
