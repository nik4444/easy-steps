package com.easysteps.model;

public class DailyStepHistory {
    String date;
    String steps_count;
    Float steps_km;
    String earned_coin;

    public DailyStepHistory(String dateList, String steps_countList, Float steps_km, String count_coninsList){
        this.date = dateList;
        this.steps_count = steps_countList;
        this.steps_km = steps_km;
        this.earned_coin = count_coninsList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSteps_count() {
        return steps_count;
    }

    public void setSteps_count(String steps_count) {
        this.steps_count = steps_count;
    }

    public Float getSteps_km() {
        return steps_km;
    }

    public void setSteps_km(Float steps_km) {
        this.steps_km = steps_km;
    }

    public String getEarned_coin() {
        return earned_coin;
    }

    public void setEarned_coin(String earned_coin) {
        this.earned_coin = earned_coin;
    }
}
