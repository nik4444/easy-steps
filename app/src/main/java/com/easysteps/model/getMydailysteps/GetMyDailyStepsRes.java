package com.easysteps.model.getMydailysteps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMyDailyStepsRes {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("addedornot")
    @Expose
    private Integer addedornot;
    @SerializedName("data")
    @Expose
    private GetMyDailyStepsData data;

    public Integer getAddedornot() {
        return addedornot;
    }

    public void setAddedornot(Integer addedornot) {
        this.addedornot = addedornot;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GetMyDailyStepsData getData() {
        return data;
    }

    public void setData(GetMyDailyStepsData data) {
        this.data = data;
    }

}
