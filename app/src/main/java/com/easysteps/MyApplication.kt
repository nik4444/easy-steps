package com.easysteps

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process
import android.webkit.WebView
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.gsonpref.gson
import com.easysteps.helper.PrefKey
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs

/**
 * Created by NIKUNJ on 02-05-2022.
 */

class MyApplication : Application() {

    private val PROCESS = "com.easysteps"

    override fun onCreate() {
        super.onCreate()

        Kotpref.init(this)
        Kotpref.gson = Gson()

        Prefs.Builder()
            .setContext(this)
            .setMode(MODE_PRIVATE)
            .setPrefsName(this.packageName)
            .setUseDefaultSharedPreference(true)
            .build()
        initPieWebView()
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                Prefs.putString(PrefKey.token, token)
                PrefKey.token = token
            }
    }

    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(context)
    }

    private fun initPieWebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = getProcessName(this)
            if (PROCESS != processName) {
                WebView.setDataDirectorySuffix(getString(processName, "sunzn")!!)
            }
        }
    }

    private fun getProcessName(context: Context?): String? {
        if (context == null) return null
        val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                return processInfo.processName
            }
        }
        return null
    }

    fun getString(s: String?, defValue: String?): String? {
        return if (isEmpty(s)) defValue else s
    }

    private fun isEmpty(s: String?): Boolean {
        return s == null || s.trim().isEmpty()
    }

}