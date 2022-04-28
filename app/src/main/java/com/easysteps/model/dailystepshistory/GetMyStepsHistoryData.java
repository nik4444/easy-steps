package com.easysteps.model.dailystepshistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMyStepsHistoryData {
    @SerializedName("StepsId")
    @Expose
    private Integer stepsId;
    @SerializedName("StepsCount")
    @Expose
    private Integer stepsCount;
    @SerializedName("StepsKm")
    @Expose
    private Double stepsKm;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("UserCoins")
    @Expose
    private Integer userCoins;
    @SerializedName("StepsDate")
    @Expose
    private String stepsDate;
    @SerializedName("CreatedAt")
    @Expose
    private String createdAt;
    @SerializedName("FormatStepsMyDate")
    @Expose
    private String formatStepsMyDate;
    @SerializedName("FormatStepsDate")
    @Expose
    private String formatStepsDate;
    @SerializedName("FormatStepsMonth")
    @Expose
    private String formatStepsMonth;
    @SerializedName("FormatStepsYear")
    @Expose
    private String formatStepsYear;

    public Integer getStepsId() {
        return stepsId;
    }

    public void setStepsId(Integer stepsId) {
        this.stepsId = stepsId;
    }

    public Integer getStepsCount() {
        return stepsCount;
    }

    public void setStepsCount(Integer stepsCount) {
        this.stepsCount = stepsCount;
    }

    public Double getStepsKm() {
        return stepsKm;
    }

    public void setStepsKm(Double stepsKm) {
        this.stepsKm = stepsKm;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserCoins() {
        return userCoins;
    }

    public void setUserCoins(Integer userCoins) {
        this.userCoins = userCoins;
    }

    public String getStepsDate() {
        return stepsDate;
    }

    public void setStepsDate(String stepsDate) {
        this.stepsDate = stepsDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormatStepsMyDate() {
        return formatStepsMyDate;
    }

    public void setFormatStepsMyDate(String formatStepsMyDate) {
        this.formatStepsMyDate = formatStepsMyDate;
    }

    public String getFormatStepsDate() {
        return formatStepsDate;
    }

    public void setFormatStepsDate(String formatStepsDate) {
        this.formatStepsDate = formatStepsDate;
    }

    public String getFormatStepsMonth() {
        return formatStepsMonth;
    }

    public void setFormatStepsMonth(String formatStepsMonth) {
        this.formatStepsMonth = formatStepsMonth;
    }

    public String getFormatStepsYear() {
        return formatStepsYear;
    }

    public void setFormatStepsYear(String formatStepsYear) {
        this.formatStepsYear = formatStepsYear;
    }

}
