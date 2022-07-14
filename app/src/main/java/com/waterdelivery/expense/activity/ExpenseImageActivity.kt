package com.waterdelivery.expense.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.waterdelivery.R
import kotlinx.android.synthetic.main.activity_expense_image.*
import kotlinx.android.synthetic.main.task_listview_layout.view.*

class ExpenseImageActivity : AppCompatActivity() {
     var image:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_image)

        image=intent.getStringExtra("image").toString()
        Glide.with(this).load(image).into(img_expense)

    }
}