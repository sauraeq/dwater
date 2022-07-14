package com.waterdelivery.user

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.gogo.gogokull.utils.ConstantUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.waterdelivery.R
import com.waterdelivery.main.DashBoardActivity
import com.waterdelivery.user.modal.LoginResponse
import com.waterdelivery.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_customer.*
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.util.*

class LoginActivity : BaseActivity(), cont {
    lateinit var sharprf: shareprefrences
    var fcmtoken: String = ""
    var longitude: String = ""
    var langitude: String = ""
    var RECORD_REQUEST_CODE: Int = 401
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharprf = shareprefrences(this)
        getToken()
        login_submit.setOnClickListener {
            validatedata()
        }
        if (checkPermission() == true) {

        } else {
            runtimePermission()
        }
    }

    private fun validatedata() {
        if (txt_username.text.toString().trim().isEmpty()) {
            txt_username.error = "Please Enter Driver ID"
            txt_username.requestFocus()
        } else if (txt_password.text.toString().trim().isEmpty()) {
            txt_password.error = "Please Enter Password"
            txt_password.requestFocus()
        } else {
            login()
        }
    }

    fun login() {
        showProgressDialog()
        val stringStringHashMap = HashMap<String, String>()
        stringStringHashMap.put("user_name", txt_username.text.toString())
        stringStringHashMap.put("password", txt_password.text.toString())
        stringStringHashMap.put("fcm_token", fcmtoken)
        var signin: Call<LoginResponse> = APIUtils.getServiceAPI()!!.signin(
            stringStringHashMap
        )
        signin.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                try {

                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status) {
                            var res = response.body()!!.data
                            sharprf.setStringPreference(USER_ID, res.id)
                            sharprf.setStringPreference(FIRST_NAME, res.name)
                            sharprf.setStringPreference(EMAIL_ID, res.email)
                            sharprf.setStringPreference(Token, response.body()!!.token)
                            sharprf.isSession(REMEBER, true)
                            toastsucess(response.body()!!.message)

                            showToastMessage(this@LoginActivity, response.body()!!.message)

                            startActivity(Intent(this@LoginActivity, DashBoardActivity::class.java))
                            finishAffinity()

                        } else {
                            toasterror(response.body()!!.message)

                        }

                    } else {
                        toasterror("Some thing went wrong")
                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                hideProgressDialog()
                toasterror(t.message.toString())

            }

        })
    }

    fun toastsucess(msg: String) {
        MotionToast.darkToast(
            this,
            "Hurray success 😍",
            msg,
            MotionToast.TOAST_SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.helvetica_regular)
        )
    }

    fun toasterror(msg: String) {

        MotionToast.darkToast(
            this,
            "Sorry ☹ ",
            msg,
            MotionToast.TOAST_ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.helvetica_regular)
        )

    }


    private fun getToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("", "getInstanceId failed", task.exception)
                return@OnCompleteListener
            }
            fcmtoken = task.result
            Log.d("genrateiontoken", "getToken: $fcmtoken")
            sharprf.setStringPreference(ConstantUtils.FCM_TOKEN, fcmtoken)
//            SharedPreferenceUtils.getInstance(this)
//                .setValue(ConstantUtils.FCM_TOKEN, token)
//            val postFcmTokenData = HashMap<String,String>()
//            postFcmTokenData.put("user_id",SharedPreferenceUtils.getInstance(mContext).getStringValue(ConstantUtils.USER_ID,""))
//            postFcmTokenData.put("fcmToken",token!!)
//            updateToken(postFcmTokenData,dialog)
        })
    }


    fun runtimePermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CALL_PHONE
            ), RECORD_REQUEST_CODE
        )
    }

    //////======if permission granted=====
    fun checkPermission(): Boolean? {
        val FirstPermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val SecondPermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val thirdPermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val FourPermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val FivePermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val SixPermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED && SecondPermissionResult == PackageManager.PERMISSION_GRANTED && thirdPermissionResult == PackageManager.PERMISSION_GRANTED && FourPermissionResult == PackageManager.PERMISSION_GRANTED && FirstPermissionResult == PackageManager.PERMISSION_GRANTED && SixPermissionResult == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    val camerapermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val WriteStoragePermission =
                        grantResults[1] == PackageManager.PERMISSION_GRANTED
                    val ReadStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED
                    val FineLocationPermission =
                        grantResults[3] == PackageManager.PERMISSION_GRANTED
                    val CoarsePermission = grantResults[4] == PackageManager.PERMISSION_GRANTED
                    if (camerapermission && WriteStoragePermission && ReadStoragePermission && FineLocationPermission && CoarsePermission) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                    }
                } else {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                }
                return
            }
        }
    }

    fun getCurrentlocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        /* val locationListener: LocationListener = object : LocationListener() {
             fun onLocationChanged(location: Location) {
                 val latitute: Double = location.getLatitude()
                 val longitute: Double = location.getLongitude()
                 Log.e("lati", latitute.toString())
                 Log.e("long", longitute.toString())
             }

             fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
             fun onProviderEnabled(provider: String?) {}
             fun onProviderDisabled(provider: String?) {}
         }*/

        val locationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                longitude = location.longitude.toString()
                langitude = location.latitude.toString()
                Log.e("langitude", location.longitude.toString())
                Log.e("langitude", location.latitude.toString())

            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        try {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                500,
                0f,
                locationListener
            )
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }
    }
}