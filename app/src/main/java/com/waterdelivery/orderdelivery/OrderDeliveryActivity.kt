package com.waterdelivery.orderdelivery


import android.content.Intent
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.R
import com.waterdelivery.store.model.StoreListResponse
import com.waterdelivery.user.LoginActivity
import com.waterdelivery.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_order_delivery.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast

class OrderDeliveryActivity : BaseActivity() {
    lateinit var shpr:shareprefrences
    lateinit var driverdata:StoreListResponse.DriverDetails
    lateinit var storedata:StoreListResponse.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_delivery)
        shpr=shareprefrences(this)
        if(intent.extras!=null){
            driverdata= intent.getSerializableExtra("driverdata") as StoreListResponse.DriverDetails
            storedata= intent.getSerializableExtra("bottledata") as StoreListResponse.Data
        }
        order_delivery_back.setOnClickListener {
            onBackPressed()
        }
        btnreurncan.setOnClickListener {
            if(ext_returncan.text.toString().trim().isEmpty()){
                toasterror("Please Enter Return Can")
            }else if(ext_brokencan.text.toString().trim().isEmpty()){
                toasterror("Please Enter Broken Can")
            }else{
                orderdilivery()
            }
        }

        btnstorencan.setOnClickListener {
            if(ect_storecan.text.toString().trim().isEmpty()){
                toasterror("Please Enter Store Can")
            }else{
                orderdilivery()
            }
        }

        btnreaming.setOnClickListener {
            if(ext_remaingingbootel.text.toString().trim().isEmpty()){
                toasterror("Please Enter Remaining  Can")
            }else{
                orderdilivery()
            }
        }
        order_photo.text=driverdata.name
        totalqauntiyt.text=storedata.quanity
        addressss.text=driverdata.address
        diliverybrand.text=storedata.store_name
        tetette.text=storedata.store_name
        emtybrndname.text=storedata.store_name
        reamingbottle.text=storedata.store_name
        Glide.with(this).load(driverdata.profile).into(expense_men)

    }


    fun orderdilivery() {
        showProgressDialog()
        var signin: Call<OrderDiliveryResponse> = APIUtils.getServiceAPI()!!.bottleOrderListUpdate(
            "Bearer " + shpr.getStringPreference("token"),
            shpr.getStringPreference("customer_id").toString(),
            storedata.id ,
                    ext_returncan.text.toString().trim(),
            ext_brokencan.text.toString().trim(),
            ect_storecan.text.toString().trim(),
            ext_remaingingbootel.text.toString().trim(),

            )
        signin.enqueue(object : Callback<OrderDiliveryResponse> {
            override fun onResponse(
                call: Call<OrderDiliveryResponse>,
                response: Response<OrderDiliveryResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()

                        if (response.body()!!.status == true) {
toastsucess(response!!.body()!!.message)
                        } else {
                            toasterror(response!!.body()!!.message)
                        }

                    }else if(response.code()==401){

                        shpr.clearAllPreferences()
                       startActivity(Intent(this@OrderDeliveryActivity, LoginActivity::class.java))
                        finishAffinity()


                    }else if(response.code()==400){
                       hideProgressDialog()
                       showToastMessage(this@OrderDeliveryActivity,response!!.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<OrderDiliveryResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }

    fun toastsucess(msg:String) {
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

    fun toasterror(msg:String){

        MotionToast.darkToast(
            this,
            "Sorry ☹ ",
            msg,
            MotionToast.TOAST_ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this,R.font.helvetica_regular))

    }
}