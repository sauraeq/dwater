package com.waterdelivery.receipt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.waterdelivery.R
import com.waterdelivery.customer.fragement.model.AddmoneyResponse
import kotlinx.android.synthetic.main.activity_receipt2.*

class ReceiptActivity : AppCompatActivity() {
    lateinit var listdata:AddmoneyResponse.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt2)
        if(intent.extras!=null){
            listdata= intent.getSerializableExtra("recepitdata") as AddmoneyResponse.Data
        }
        rechange_ammount.text="AED " +listdata.amt
        txt_ammount.text="AED " +listdata.amt
        text_customerid.text="Customer ID  " +listdata.customer_id
        txt_txnid.text=listdata.id
        txt_customername.text=listdata.driver_name
        driver_id.text=listdata.created_by
        textdate.text=listdata.created_date
        recipet_back.setOnClickListener {
            onBackPressed()
        }
    }
}