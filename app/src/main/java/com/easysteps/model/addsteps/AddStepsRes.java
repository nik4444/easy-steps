package com.easysteps.model.addsteps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddStepsRes {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private AddStepsData data;

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

    public AddStepsData getData() {
        return data;
    }

    public void setData(AddStepsData data) {
        this.data = data;
    }


}