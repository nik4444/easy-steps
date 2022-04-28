package com.easysteps.repository;

import com.easysteps.network.ApiCallInterface;
import com.google.gson.JsonElement;

import java.util.HashMap;

import io.reactivex.Observable;

/**
 * Created by NIKUNJ
 */
public class Repository {

    private final ApiCallInterface apiCallInterface;

    public Repository(ApiCallInterface apiCallInterface) {
        this.apiCallInterface = apiCallInterface;
    }

    public Observable<JsonElement> login(HashMap<String, String> body) {
        return apiCallInterface.login(body);
    }

}
