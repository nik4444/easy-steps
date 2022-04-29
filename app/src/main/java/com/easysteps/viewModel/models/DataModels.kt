package com.easysteps.viewModel.models

/**
 * Created by NIKUNJ on 29-04-2022.
 */

data class Login(
    val status: Int? = null,
    val message: String? = null,
    val data: SignupData
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