package com.easysteps.model.addsteps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddStepsData {
    @SerializedName("StepsCount")
    @Expose
    private Integer stepsCount;
    @SerializedName("UserCoins")
    @Expose
    private String userCoins;
    @SerializedName("TodayUserCoins")
    @Expose
    private String todayUserCoins;

    @SerializedName("StepsKm")
    @Expose
    private Double stepsKm;

    public String getTodayUserCoins() {
        return todayUserCoins;
    }

    public void setTodayUserCoins(String todayUserCoins) {
        this.todayUserCoins = todayUserCoins;
    }

    public Integer getStepsCount() {
        return stepsCount;
    }

    public void setStepsCount(Integer stepsCount) {
        this.stepsCount = stepsCount;
    }

    public String getUserCoins() {
        return userCoins;
    }

    public void setUserCoins(String userCoins) {
        this.userCoins = userCoins;
    }

    public Double getStepsKm() {
        return stepsKm;
    }

    public void setStepsKm(Double stepsKm) {
        this.stepsKm = stepsKm;
    }

}
