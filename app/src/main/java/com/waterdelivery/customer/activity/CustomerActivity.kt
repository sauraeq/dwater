package com.waterdelivery.customer.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.R
import com.waterdelivery.customer.modal.AddCustomerResponse
import com.waterdelivery.main.DashBoardActivity
import com.waterdelivery.notification.NotificationActivity
import com.waterdelivery.user.LoginActivity
import com.waterdelivery.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_customer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.util.*

class CustomerActivity : BaseActivity(),cont {
    lateinit var sharprf: shareprefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)
        sharprf = shareprefrences(this)
        sumit_from.setOnClickListener {
            validatedata()
        }
        notification.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
    }


    private fun validatedata() {
        if (ext_name.text.toString().trim().isEmpty()) {
            toasterror("Please Enter Name")
        } else if (ext_email.text.toString().trim().isEmpty()) {
            toasterror("Please Enter Email id")

        } else if (!emailValidator(ext_email.text.toString())) {
            toasterror("Please Enter Vaild Email id")

        } else if ((ext_phonenumber.text.toString().isEmpty())) {
            toasterror("Please Enter phone Number")

        } else if ((ext_phonenumber.text.toString().length != 10)) {
            toasterror("Please Enter Vaild phone Number")

        } else {
            updatedata()
        }
    }

    fun updatedata() {
        showProgressDialog()
        val stringStringHashMap = HashMap<String, String>()
        stringStringHashMap.put("name", ext_name.text.toString())
        stringStringHashMap.put("email", ext_email.text.toString())
        stringStringHashMap.put("driver_id", ext_email.text.toString())
        stringStringHashMap.put("phone", ext_phonenumber.text.toString())
        var signin: Call<AddCustomerResponse> = APIUtils.getServiceAPI()!!.addcusmtomer(
            "Bearer " + sharprf.getStringPreference("token"),
            ext_name.text.toString(),
            ext_email.text.toString(),
            ext_phonenumber.text.toString(),
            sharprf.getStringPreference(USER_ID).toString()
        )
        signin.enqueue(object : Callback<AddCustomerResponse> {
            override fun onResponse(
                call: Call<AddCustomerResponse>,
                response: Response<AddCustomerResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == true) {
                            toastsucess(response.body()!!.message)
                            ext_name.setText("")
                            ext_email.setText("")
                            ext_phonenumber.setText("")
                            startActivity(
                                Intent(
                                    this@CustomerActivity,
                                    DashBoardActivity::class.java
                                )
                            )
                            finish()

                        } else {
                            showToastMessage(this@CustomerActivity, response.body()!!.message)
                        }

                    } else if (response.code() == 401) {

                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@CustomerActivity, LoginActivity::class.java))
                        finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@CustomerActivity, response.body()!!.message)

                    } else if (response.code() == 500) {
                        hideProgressDialog()
//                        mainacivity.showToastMessage(context,response!!.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<AddCustomerResponse>, t: Throwable) {
                hideProgressDialog()
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
}