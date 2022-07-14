package com.waterdelivery.Shop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.Cart.Adapter.CountResponse
import com.waterdelivery.Cart.CartActivity
import com.waterdelivery.R
import com.waterdelivery.Shop.adapter.ShopAdapter
import com.waterdelivery.user.LoginActivity
import com.waterdelivery.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_shop.*
import kotlinx.android.synthetic.main.fragment_task_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopActivity : BaseActivity() {
    lateinit var sharprf: shareprefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        sharprf = shareprefrences(this)
        backreturnshop.setOnClickListener {
            onBackPressed()
        }
        gotoshoping.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }


    fun productlist() {
        showProgressDialog()
        var signin: Call<ProductListResponse> = APIUtils.getServiceAPI()!!.productlist(
            "Bearer " + sharprf.getStringPreference("token"),
            sharprf.getStringPreference("customer_id").toString()
        )
        signin.enqueue(object : Callback<ProductListResponse> {
            override fun onResponse(
                call: Call<ProductListResponse>,
                response: Response<ProductListResponse>
            ) {
                try {

                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == true) {
                            shop_recycle.layoutManager = GridLayoutManager(this@ShopActivity, 2)
                            shop_recycle.adapter = ShopAdapter(
                                this@ShopActivity,
                                response.body()!!.data as ArrayList<ProductListResponse.Data>
                            )
                        } else {

                        }

                    } else if (response.code() == 401) {
                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@ShopActivity, LoginActivity::class.java))
                        finishAffinity()

                    }
                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                hideProgressDialog()
                Log.e("hello", t.message.toString())
            }

        })
    }



    fun count() {
        showProgressDialog()
        var signin: Call<CountResponse> = APIUtils.getServiceAPI()!!.count(
            "Bearer " + sharprf.getStringPreference("token"),
            sharprf.getStringPreference("customer_id").toString(),
        )
        signin.enqueue(object : Callback<CountResponse> {
            override fun onResponse(
                call: Call<CountResponse>,
                response: Response<CountResponse>
            ) {
                try {

                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == true) {

                            txt_count.text=response!!.body()!!.TotalCount.toString()
                        } else {

                        }

                    } else if (response.code() == 401) {
                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@ShopActivity, LoginActivity::class.java))
                        finishAffinity()

                    }
                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<CountResponse>, t: Throwable) {
                hideProgressDialog()
                Log.e("hello", t.message.toString())
            }

        })
    }

    override fun onResume() {
        super.onResume()
        productlist()
        count()
    }
}