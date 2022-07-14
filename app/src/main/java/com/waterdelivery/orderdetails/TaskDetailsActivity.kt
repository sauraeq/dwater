package com.waterdelivery.orderdetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.waterdelivery.R
import com.waterdelivery.task.modal.TaskListResponse
import kotlinx.android.synthetic.main.activity_task_details.*

class TaskDetailsActivity : AppCompatActivity() {
    lateinit var taskdata: TaskListResponse.Data.Order
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)
        recipet_back.setOnClickListener {
            onBackPressed()
        }
        if (intent.extras != null) {
            taskdata = intent.getSerializableExtra("taskdata") as TaskListResponse.Data.Order
        }
        taskdetailsrecycle.layoutManager = LinearLayoutManager(this)
        taskdetailsrecycle.adapter = TaskDeatilsAdapter(
            this,
            taskdata.product as ArrayList<TaskListResponse.Data.Order.Product>
        )

        try {
            status.text = taskdata.delever_status
            txt_orderid.text = taskdata.order_id

            txt_phone.text = taskdata.address.phone_no
            txt_grossammount.text = "AED " + taskdata.amt_without_tax.toString()
            txt_vat.text = "AED " + taskdata.amt_tax.toString()
            txt_total.text = "AED " + taskdata.total_amt
            txt_customername.text =  taskdata.name
            txt_address.text =
                taskdata.address.address + ", " + taskdata.address.city + ", " + taskdata.address.district
        }catch (e:Exception){

        }

    }
}