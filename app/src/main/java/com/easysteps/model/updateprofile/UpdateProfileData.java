package com.easysteps.model.updateprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfileData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userEmail")
    @Expose
    private String userEmail;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userAddress")
    @Expose
    private String userAddress;
    @SerializedName("userCity")
    @Expose
    private String userCity;
    @SerializedName("userPostCode")
    @Expose
    private String userPostCode;
    @SerializedName("userSteps")
    @Expose
    private Integer userSteps;
    @SerializedName("userCountry")
    @Expose
    private String userCountry;
    @SerializedName("userState")
    @Expose
    private String userState;
    @SerializedName("email_verified_at")
    @Expose
    private String emailVerifiedAt;
    @SerializedName("userPhone")
    @Expose
    private String userPhone;
    @SerializedName("userOtp")
    @Expose
    private Integer userOtp;
    @SerializedName("userProfile")
    @Expose
    private String userProfile;
    @SerializedName("userBlocked")
    @Expose
    private Integer userBlocked;
    @SerializedName("userLongitude")
    @Expose
    private String userLongitude;
    @SerializedName("userLatitude")
    @Expose
    private String userLatitude;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserPostCode() {
        return userPostCode;
    }

    public void setUserPostCode(String userPostCode) {
        this.userPostCode = userPostCode;
    }

    public Integer getUserSteps() {
        return userSteps;
    }

    public void setUserSteps(Integer userSteps) {
        this.userSteps = userSteps;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(String emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getUserOtp() {
        return userOtp;
    }

    public void setUserOtp(Integer userOtp) {
        this.userOtp = userOtp;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public Integer getUserBlocked() {
        return userBlocked;
    }

    public void setUserBlocked(Integer userBlocked) {
        this.userBlocked = userBlocked;
    }

    public String getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(String userLongitude) {
        this.userLongitude = userLongitude;
    }

    public String getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(String userLatitude) {
        this.userLatitude = userLatitude;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
