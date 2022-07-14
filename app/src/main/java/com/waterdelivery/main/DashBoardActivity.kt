package com.waterdelivery.main

import android.Manifest
import android.app.ActivityManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.equalinfotech.learnorteach.constant.cont
import com.waterdelivery.R
import com.waterdelivery.Serivice.LocationService
import com.waterdelivery.customer.fragement.CustomerFragment
import com.waterdelivery.expense.activity.ExpenseFragment
import com.waterdelivery.notification.NotificationActivity
import com.waterdelivery.returndata.ReturnActivity
import com.waterdelivery.store.StorekFragment
import com.waterdelivery.task.fragement.TaskFragment
import com.waterdelivery.user.ProfileFragment
import com.waterdelivery.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_dash_board.*

class DashBoardActivity : BaseActivity(), cont {
    var longitude: String? = ""
    var langitude: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        texttitle.text="Customer Management"
        loadFragment(CustomerFragment())
        bottomBar()
        startLocationService()
        notificationhome.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        profile_tab.setOnClickListener {
            header.visibility=View.GONE
            texttitle.text="Profile"
            loadFragment(ProfileFragment())
        }
    }


    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
//        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun bottomBar() {
        navigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    header.visibility=View.VISIBLE
                    texttitle.text="Customer Management"
                    loadFragment(CustomerFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_class -> {
                    header.visibility=View.VISIBLE
                    texttitle.text="Task Management"
                    loadFragment(TaskFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_perals -> {
                    header.visibility=View.VISIBLE
                    texttitle.text="Expense"
                    loadFragment(ExpenseFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_qbank -> {
                    header.visibility=View.VISIBLE
                    texttitle.text="Stock"
                    loadFragment(StorekFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_exm -> {
                    header.visibility=View.VISIBLE
//                    loadFragment(ProfileFragment())
                    startActivity(Intent(this, ReturnActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }
    }


    private fun startLocationService() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        } else {
            if (!isLocationServiceRunning()) {
                val intent1 = Intent(applicationContext, LocationService::class.java)
                intent1.action = "startlocationservice"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startService(intent1)
                }
            }
        }
    }


    private fun isLocationServiceRunning(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        if (activityManager != null) {
            for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
                if (LocationService::class.java.name == service.service.className) {
                    if (service.foreground) {
                        return true
                    }
                }
            }
            return false
        }
        return false
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}