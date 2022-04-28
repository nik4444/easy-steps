package com.easysteps.model.getMydailysteps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetMyDailyStepsData {
    @SerializedName("StepsCount")
    @Expose
    private Integer stepsCount;
    @SerializedName("UserCoins")
    @Expose
    private Integer userCoins;
    @SerializedName("TodayUserCoins")
    @Expose
    private Integer todayUserCoins;
    @SerializedName("StepsKm")
    @Expose
    private Double stepsKm;
    @SerializedName("RewardedData")
    @Expose
    private List<RewardedDatum> rewardedData = null;

    public Integer getStepsCount() {
        return stepsCount;
    }

    public void setStepsCount(Integer stepsCount) {
        this.stepsCount = stepsCount;
    }

    public Integer getUserCoins() {
        return userCoins;
    }

    public void setUserCoins(Integer userCoins) {
        this.userCoins = userCoins;
    }

    public Integer getTodayUserCoins() {
        return todayUserCoins;
    }

    public void setTodayUserCoins(Integer todayUserCoins) {
        this.todayUserCoins = todayUserCoins;
    }

    public Double getStepsKm() {
        return stepsKm;
    }

    public void setStepsKm(Double stepsKm) {
        this.stepsKm = stepsKm;
    }

    public List<RewardedDatum> getRewardedData() {
        return rewardedData;
    }

    public void setRewardedData(List<RewardedDatum> rewardedData) {
        this.rewardedData = rewardedData;
    }

}
