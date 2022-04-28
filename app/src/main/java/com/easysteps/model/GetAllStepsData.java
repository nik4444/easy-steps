package com.easysteps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAllStepsData {
    @SerializedName("StepsCount")
    @Expose
    private String stepsCount;
    @SerializedName("UserCoins")
    @Expose
    private Integer userCoins;
    @SerializedName("StepsKm")
    @Expose
    private String stepsKm;

    public String getStepsCount() {
        return stepsCount;
    }

    public void setStepsCount(String stepsCount) {
        this.stepsCount = stepsCount;
    }

    public Integer getUserCoins() {
        return userCoins;
    }

    public void setUserCoins(Integer userCoins) {
        this.userCoins = userCoins;
    }

    public String getStepsKm() {
        return stepsKm;
    }

    public void setStepsKm(String stepsKm) {
        this.stepsKm = stepsKm;
    }
}
