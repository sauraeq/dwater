package com.waterdelivery.notification

import NotificationAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.R
import com.waterdelivery.user.LoginActivity
import com.waterdelivery.user.modal.GetProfileResponse
import com.waterdelivery.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActivity : BaseActivity() {
    lateinit var sharprf:shareprefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        sharprf= shareprefrences(this)
        notification_back.setOnClickListener {
            onBackPressed()
        }
        notification()
    }

    fun notification() {
        showProgressDialog()
        var notification: Call<NotificationResponse> = APIUtils.getServiceAPI()!!.getNotification(
            "Bearer " + sharprf.getStringPreference("token"),
            sharprf.getStringPreference("customer_id").toString()
        )
        notification.enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(
                call: Call<NotificationResponse>,
                response: Response<NotificationResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == true) {

                            recy_notification.layoutManager=LinearLayoutManager(this@NotificationActivity)
                            recy_notification.adapter=NotificationAdapter(this@NotificationActivity,
                                response!!.body()!!.data as ArrayList<NotificationResponse.Data>
                            )

                        } else {

                        }

                    } else if (response.code() == 401) {
                      hideProgressDialog()
                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@NotificationActivity, LoginActivity::class.java))
                        finishAffinity()


                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }
}