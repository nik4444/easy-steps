package com.easysteps.model;

public class DealRes {

    String image;
    String ad_new;
    String deal_name;
    String deal_point;
    String isRunning;
    String deal_link;
    String deal_file_type;

    public DealRes(String image, String ad_new, String deal_name, String deal_point, String isRunning,String deal_link,String deal_file_type) {
        this.image = image;
        this.ad_new = ad_new;
        this.deal_name = deal_name;
        this.deal_point = deal_point;
        this.isRunning = isRunning;
        this.deal_link = deal_link;
        this.deal_file_type = deal_file_type;
    }

    public String getDeal_file_type() {
        return deal_file_type;
    }

    public void setDeal_file_type(String deal_file_type) {
        this.deal_file_type = deal_file_type;
    }

    public String getDeal_link() {
        return deal_link;
    }

    public void setDeal_link(String deal_link) {
        this.deal_link = deal_link;
    }

    public String getDeal_point() {
        return deal_point;
    }

    public void setDeal_point(String deal_point) {
        this.deal_point = deal_point;
    }

    public String getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(String isRunning) {
        this.isRunning = isRunning;
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
