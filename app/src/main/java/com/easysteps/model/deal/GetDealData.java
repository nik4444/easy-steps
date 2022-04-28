package com.easysteps.model.deal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDealData {

    @SerializedName("DealId")
    @Expose
    private Integer dealId;
    @SerializedName("DealTitle")
    @Expose
    private String dealTitle;
    @SerializedName("DealFileType")
    @Expose
    private String DealFileType;
    @SerializedName("Dealpoints")
    @Expose
    private Integer dealpoints;
    @SerializedName("DealStatus")
    @Expose
    private Integer dealStatus;
    @SerializedName("DealPicture")
    @Expose
    private String dealPicture;
    @SerializedName("IsRunning")
    @Expose
    private Integer isRunning;
    @SerializedName("CreatedAt")
    @Expose
    private String createdAt;
    @SerializedName("DealLink")
    @Expose
    private String DealLink;

    public String getDealFileType() {
        return DealFileType;
    }

    public void setDealFileType(String dealFileType) {
        DealFileType = dealFileType;
    }

    public String getDealLink() {
        return DealLink;
    }

    public void setDealLink(String dealLink) {
        DealLink = dealLink;
    }

    public Integer getDealId() {
        return dealId;
    }

    public void setDealId(Integer dealId) {
        this.dealId = dealId;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    public Integer getDealpoints() {
        return dealpoints;
    }

    public void setDealpoints(Integer dealpoints) {
        this.dealpoints = dealpoints;
    }

    public Integer getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }

    public String getDealPicture() {
        return dealPicture;
    }

    public void setDealPicture(String dealPicture) {
        this.dealPicture = dealPicture;
    }

    public Integer getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(Integer isRunning) {
        this.isRunning = isRunning;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
