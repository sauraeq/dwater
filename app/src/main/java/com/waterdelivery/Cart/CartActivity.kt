package com.waterdelivery.Cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.Cart.Adapter.CartAdapter
import com.waterdelivery.Checkout.CheckoutActivity
import com.waterdelivery.R
import com.waterdelivery.task.adpater.DateAdapter
import com.waterdelivery.task.modal.TaskListResponse
import com.waterdelivery.user.LoginActivity
import com.waterdelivery.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.fragment_task_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : BaseActivity() {
    lateinit var sharprf:shareprefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        sharprf= shareprefrences(this)

        btn_checkout.setOnClickListener {
            startActivity(Intent(this,CheckoutActivity::class.java))
        }
        backreturncart.setOnClickListener {
            onBackPressed()
        }
    }

    fun cartlist() {
        showProgressDialog()
        var signin: Call<CartListResponse> = APIUtils.getServiceAPI()!!.cartlist(
            "Bearer " + sharprf.getStringPreference("token"),
            sharprf.getStringPreference("customer_id").toString()
        )
        signin.enqueue(object : Callback<CartListResponse> {
            override fun onResponse(
                call: Call<CartListResponse>,
                response: Response<CartListResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == true) {
                            cart_recycle.layoutManager=LinearLayoutManager(this@CartActivity)
                            cart_recycle.adapter=CartAdapter(this@CartActivity,
                                response!!.body()!!.data as ArrayList<CartListResponse.Data>,carterror,btn_checkout
                            )
                            if(response!!.body()!!.data.size>0){
                                carterror.visibility=View.GONE
                            }else{
                                carterror.visibility=View.VISIBLE
                            }

                        } else {

                        }

                    } else if (response.code() == 401) {
                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@CartActivity, LoginActivity::class.java))
                       finishAffinity()

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                hideProgressDialog()
                Log.e("hello", t.message.toString())
            }

        })

    }

    override fun onResume() {
        super.onResume()
        cartlist()
    }
}