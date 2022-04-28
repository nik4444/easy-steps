package com.easysteps.model.getMydailysteps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RewardedDatum {
    @SerializedName("RewardedId")
    @Expose
    private Integer rewardedId;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Order")
    @Expose
    private Integer order;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("Coins")
    @Expose
    private Integer coins;
    @SerializedName("is_performed")
    @Expose
    private Integer isPerformed;

    public Integer getRewardedId() {
        return rewardedId;
    }

    public void setRewardedId(Integer rewardedId) {
        this.rewardedId = rewardedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Integer getIsPerformed() {
        return isPerformed;
    }

    public void setIsPerformed(Integer isPerformed) {
        this.isPerformed = isPerformed;
    }

}
