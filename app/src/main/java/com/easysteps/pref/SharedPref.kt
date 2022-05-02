package com.easysteps.pref

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.gsonpref.gsonNullablePref
import com.easysteps.viewModel.models.SignupData

object SharedPref : KotprefModel() {

    var authToken by stringPref()

    var fcmToken by stringPref("")
    var rememberEmail by stringPref("")
    var rememberPassword by stringPref("")

    var isLogin by booleanPref()
    var isRemember by booleanPref(false)

    var signupData by gsonNullablePref<SignupData>()

    fun clearPref() {
        clear()
    }
}