package com.easysteps.retrofit;

public class URLs {

    public static String IMAGE_URL = "http://46.101.95.217/easysteps/";
    private static String MAIN_URL = "http://46.101.95.217/easysteps/api/";

    public static String REGISTER() {
        return MAIN_URL + "registration";
    }
    public static String LOGIN() {
        return MAIN_URL + "signIn";
    }
    public static String UPDATE_PROFILE() {
        return MAIN_URL + "UpdateProfile";
    }
    public static String LOGOUT() {
        return MAIN_URL + "Logout";
    }
    public static String DELETE_ACCOUNT() {
        return MAIN_URL + "DeleteAccount";
    }
    public static String FORGOT_PASSWORD() {
        return MAIN_URL + "ForgotPassword";
    }
    public static String ADD_MY_DAILY_STEPS() {
        return MAIN_URL + "AddMyDailySteps";
    }
    public static String GET_DEALS_DATA() {
        return MAIN_URL + "GetDealsData";
    }
    public static String CONTACTUS() {
        return MAIN_URL + "ContactUsForMe";
    }
    public static String GET_MY_STEPS_HISTORY() {
        return MAIN_URL + "GetMyStepsHistory";
    }
    public static String CHNAGE_PASSWORD() {
        return MAIN_URL + "ChangePassword";
    }
    public static String RESET_PASSWORD() {
        return MAIN_URL + "ResetPassword";
    }
    public static String GET_MY_DAILY_STEPS() {
        return MAIN_URL + "GetMyDailySteps";
    }
    public static String UPDATE_REGIONLAG() {
        return MAIN_URL + "UpdateRegionLang";
    }
    public static String ADD_TO_ACCEPT_REWARD() {
        return MAIN_URL + "AddToAcceptReward";
    }
    public static String UPDATE_MY_DAILY_STEPS() {
        return MAIN_URL + "UpdateMyDailySteps";
    }

}
