package com.easysteps.pref

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.gsonpref.gsonNullablePref
import com.easysteps.viewModel.models.ProfileUpdate
import com.easysteps.viewModel.models.SignupData

object SharedPref : KotprefModel() {

    var authToken by stringPref("testToken")

    var language by stringPref("en")
    var rememberEmail by stringPref("")
    var rememberPassword by stringPref("")
    var registerName by stringPref("")

    var f1327sp by stringPref("20000")
    var distanceMeasure by booleanPref(false)
    var condition_start_steps by intPref(0)
    var condition_end_steps by intPref(0)
    var current_steps by intPref(0)
    var last_steps by intPref(0)

    var todayDate by stringPref("")

    var isLogin by booleanPref()
    var isRemember by booleanPref(false)
    var isFaceLocked by booleanPref(false)
    var account_first_time by booleanPref(false)

    var signupData by gsonNullablePref<SignupData>()
    var updateProfile by gsonNullablePref<ProfileUpdate>()

    fun clearPref() {
        clear()
    }
}