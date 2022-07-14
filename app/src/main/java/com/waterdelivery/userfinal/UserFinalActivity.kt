package com.waterdelivery.userfinal

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.R
import com.waterdelivery.user.LoginActivity
import com.waterdelivery.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_user_final.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFinalActivity : BaseActivity(), cont {
    lateinit var sharprf: shareprefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_final)
        sharprf = shareprefrences(this)
        profile()
        backreturn.setOnClickListener {
            onBackPressed()
        }

    }


    fun profile() {
        showProgressDialog()
        var signin: Call<ActivityRespons> = APIUtils.getServiceAPI()!!.activity(
            "Bearer " + sharprf.getStringPreference("token"),
            sharprf.getStringPreference("customer_id").toString()
        )
        signin.enqueue(object : Callback<ActivityRespons> {
            override fun onResponse(
                call: Call<ActivityRespons>,
                response: Response<ActivityRespons>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == true) {
                            activity_recycle.layoutManager =
                                LinearLayoutManager(this@UserFinalActivity)
                            activity_recycle.adapter = UserDateAdapter(
                                this@UserFinalActivity,
                                response.body()!!.data as ArrayList<ActivityRespons.Data>
                            )


                        } else {

                        }

                    } else if (response.code() == 401) {
                        hideProgressDialog()
                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@UserFinalActivity, LoginActivity::class.java))
                        finishAffinity()


                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<ActivityRespons>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }


}