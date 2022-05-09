package com.easysteps.viewModel.models

/**
 * Created by NIKUNJ on 29-04-2022.
 */

data class BaseResponse<out T>(
    var status: Int,
    var addedornot: Int = 0,
    val message: String = "",
    val data: T? = null
)

data class BaseResponses<out T>(
    var status: Int,
    var addedornot: Int = 0,
    val message: String = "",
    val data: List<T>? = null
)

data class SignupData(
    var id: Int? = null,
    var userEmail: String? = null,
    var userName: String? = null,
    var userAddress: String? = null,
    var userCity: String? = null,
    var userPostCode: String? = null,
    var userSteps: Int? = null,
    var userCountry: String? = null,
    var emailVerifiedAt: String? = null,
    var userState: String? = null,
    var mOtp: Int? = null,
    var userPhone: String? = null,
    var userOtp: Int? = null,
    var userProfile: String? = null,
    var userBlocked: Int? = null,
    var userLongitude: String? = null,
    var userLatitude: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null,
    var userDeviceToken: String? = null,
    var userDevice: String? = null,
    var userDeviceId: String? = null,
    var userId: Int? = null,
    var userToken: String? = null
)

data class StepData(
    val stepsCount: Int,
    val userCoins: String,
    val todayUserCoins: String,
    val stepsKm: Double,
)

data class GetDailyStepData(
    val stepsCount: Int,
    val userCoins: Int,
    val todayUserCoins: Int,
    val stepsKm: Double,
    val RewardedData: List<RewardData>
)

data class ProfileUpdate(
    var id: Int? = null,
    var userEmail: String? = null,
    var userName: String? = null,
    var userAddress: String? = null,
    var userCity: String? = null,
    var userPostCode: String? = null,
    var userSteps: Int? = null,
    var userCountry: String? = null,
    var userState: String? = null,
    var email_verified_at: String? = null,
    var userPhone: String? = null,
    var userProfile: String? = null,
    var userBlocked: String? = null,
    var userLongitude: String? = null,
    var userLatitude: String? = null,
)

data class RewardData(
    val rewardedId: Int,
    val order: Int,
    val coins: Int,
    var isPerformed: Int,
    val title: String,
    val createdAt: String,
)

data class GetDealData(
    var dealId: Int,
    val DealTitle: String,
    val DealFileType: String,
    val DealPicture: String,
    val DealLink: String,
    val Dealpoints: Int,
    val IsRunning: Int,
    val DealStatus: Int,
)

data class GetMyStepsHistoryData(
    var StepsId: Int,
    var UserId: Int,
    var StepsCount: Int,
    var UserCoins: Int,
    var StepsKm: Double,
    val DealTitle: String,
    val CreatedAt: String,
    val FormatStepsMyDate: String,
    val FormatStepsDate: String,
    val FormatStepsMonth: String,
    val FormatStepsYear: String,
    val StepsDate: String,
)