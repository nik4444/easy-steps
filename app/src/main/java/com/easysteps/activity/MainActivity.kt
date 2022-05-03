package com.easysteps.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.easysteps.R
import com.easysteps.base.BaseActivity
import com.easysteps.databinding.ActivityMainBinding
import com.easysteps.fragment.DailyStepHistoryFragment
import com.easysteps.fragment.DealFragment
import com.easysteps.fragment.HomeFragment
import com.easysteps.fragment.SettingsFragment
import com.easysteps.helper.PrefKey
import com.easysteps.pref.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pixplicity.easyprefs.library.Prefs
import java.util.*

/**
 * Created by NIKUNJ on 03-05-2022.
 */
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), View.OnClickListener {

    private val GOOGLE_FIT_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        setOnClickListener()
    }

    private fun changeLanguage(lan: String) {
        val res: Resources = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(lan))
        res.updateConfiguration(conf, dm)
    }

    private fun setOnClickListener() {

    }

    private fun initView() {
        changeLanguage(SharedPref.language)
        loadFragment(HomeFragment())

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            try {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.ACCESS_FINE_LOCATION),
                    0
                )
            } catch (e: Exception) {
            }
        }

        binding.bottomNavigation.itemIconTintList = null
        binding.bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    changeFragment(
                        HomeFragment(), HomeFragment::class.java
                            .simpleName
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_time -> {
                    changeFragment(
                        DailyStepHistoryFragment(), DailyStepHistoryFragment::class.java
                            .simpleName
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_gift -> {
                    changeFragment(
                        DealFragment(), DealFragment::class.java
                            .simpleName
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_settings -> {
                    changeFragment(
                        SettingsFragment(), SettingsFragment::class.java
                            .simpleName
                    )
                    return@OnNavigationItemSelectedListener true
                }
                else -> true
            }
        })

    }

    fun loadFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameContainer, fragment!!)
        transaction.commit()
    }

    fun changeFragment(fragment: Fragment?, tagFragmentName: String?) {
        val mFragmentManager = supportFragmentManager
        val fragmentTransaction = mFragmentManager.beginTransaction()
        val currentFragment = mFragmentManager.primaryNavigationFragment
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment)
        }
        var fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName)
        if (fragmentTemp == null) {
            fragmentTemp = fragment
            fragmentTransaction.replace(R.id.frameContainer, fragmentTemp!!, tagFragmentName)
        } else {
            fragmentTransaction.show(fragmentTemp)
        }
        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commitNowAllowingStateLoss()
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSION_REQUEST_CODE) {
                Prefs.putBoolean(PrefKey.account_first_time, true)
                Handler().postDelayed({ loadFragment(HomeFragment()) }, 1500)
            }
        } else {
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        when (requestCode) {
            0 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission are Granted", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        for (frag in fm.fragments) {
            if (frag.isVisible) {
                val childFm = frag.childFragmentManager
                if (childFm.backStackEntryCount > 0) {
                    childFm.popBackStack()
                    return
                }
            }
        }
        super.onBackPressed()
    }
}