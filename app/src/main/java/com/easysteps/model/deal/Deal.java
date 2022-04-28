package com.easysteps.model.deal;

public class Deal {

    String image;
    String ad_new;
    String deal_name;

    public Deal(String image, String ad_new, String deal_name) {
        this.image = image;
        this.ad_new = ad_new;
        this.deal_name = deal_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAd_new() {
        return ad_new;
    }

    public void setAd_new(String ad_new) {
        this.ad_new = ad_new;
    }

    public String getDeal_name() {
        return deal_name;
    }

    public void setDeal_name(String deal_name) {
        this.deal_name = deal_name;
    }
}
