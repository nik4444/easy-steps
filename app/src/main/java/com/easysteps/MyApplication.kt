package com.easysteps

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Process
import android.webkit.WebView
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.gsonpref.gson
import com.easysteps.multilanguage.LocaleManager
import com.google.gson.Gson

/**
 * Created by NIKUNJ on 02-05-2022.
 */

class MyApplication : Application(), LifecycleObserver {

    private val PROCESS = "com.easysteps"

    companion object {
        private lateinit var mInstance: MyApplication

        @Synchronized
        fun getInstance(): MyApplication = mInstance
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        Kotpref.init(this)
        Kotpref.gson = Gson()

        initPieWebView()
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

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleManager.setLocale(this)
    }

}