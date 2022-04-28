package com.easysteps.model.dailystepshistory;

public class StepsHistoryRes {

    String StepsCount;
    String StepsKm;
    String UserCoins;
    String FormatStepsDate;
    String FormatStepsMonth;
    String FormatStepsYear;

    public StepsHistoryRes(String stepsCount, String stepsKm, String userCoins ,String FormatStepsDate ,String FormatStepsMonth ,String FormatStepsYear) {
        this.StepsCount = stepsCount;
        this.StepsKm = stepsKm;
        this.UserCoins = userCoins;
        this.FormatStepsDate = FormatStepsDate;
        this.FormatStepsMonth = FormatStepsMonth;
        this.FormatStepsYear = FormatStepsYear;
    }

    public String getFormatStepsDate() {
        return FormatStepsDate;
    }

    public void setFormatStepsDate(String formatStepsDate) {
        FormatStepsDate = formatStepsDate;
    }

    public String getFormatStepsMonth() {
        return FormatStepsMonth;
    }

    public void setFormatStepsMonth(String formatStepsMonth) {
        FormatStepsMonth = formatStepsMonth;
    }

    public String getFormatStepsYear() {
        return FormatStepsYear;
    }

    public void setFormatStepsYear(String formatStepsYear) {
        FormatStepsYear = formatStepsYear;
    }

    public String getStepsCount() {
        return StepsCount;
    }

    public void setStepsCount(String stepsCount) {
        StepsCount = stepsCount;
    }

    public String getStepsKm() {
        return StepsKm;
    }

    public void setStepsKm(String stepsKm) {
        StepsKm = stepsKm;
    }

    public String getUserCoins() {
        return UserCoins;
    }

    public void setUserCoins(String userCoins) {
        UserCoins = userCoins;
    }

}
