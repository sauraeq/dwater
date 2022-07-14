package com.waterdelivery.returndata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.waterdelivery.R
import com.waterdelivery.ReturndataResponse
import kotlinx.android.synthetic.main.activity_return_details.*

class ReturnDetailsActivity : AppCompatActivity() {
    lateinit var data: ReturndataResponse.Data.ReturnCustomer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_details)
        if(intent.extras!=null){
            data= intent.getSerializableExtra("returndatalist") as ReturndataResponse.Data.ReturnCustomer
        }
        backreturndetails.setOnClickListener {
            onBackPressed()
        }
        txt_date.text=data.DATEFORMAT
        txt_time.text=data.TIMEFORMAT
        txt_product.text=data.product_type
        txt_qty.text=data.quantity
        txt_Serial.text=data.serial_number
        txt_remark.text=data.remark
        customer_name.text=data.name
        customer_number.text=data.mobile_number
        Glide.with(this).load(data.profile).into(customer_profile)
    }
}