package com.waterdelivery.returndata.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.waterdelivery.R
import com.waterdelivery.ReturndataResponse
import com.waterdelivery.returndata.ReturnDetailsActivity
import kotlinx.android.synthetic.main.adapter_return.view.*

class ReturnAdapter(
    var context: Context,
    var date:String,
    var returnlistt: ArrayList<ReturndataResponse.Data.ReturnCustomer>,
) : RecyclerView.Adapter<ReturnAdapter.ReturnHolder>() {
    inner class ReturnHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.details_btn.setOnClickListener {
                var intent = Intent(context, ReturnDetailsActivity::class.java)
                intent.putExtra("returndatalist", returnlistt[adapterPosition])
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturnHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_return, parent, false)
        return ReturnHolder(view)
    }

    override fun onBindViewHolder(holder: ReturnHolder, position: Int) {

        Glide.with(context).load(returnlistt[position].profile).into(holder.itemView.expense_profile)
        holder.itemView.cutomer_name.text = returnlistt[position].name
        holder.itemView.returnphone.text = returnlistt[position].mobile_number
        holder.itemView.returnquanity.text = returnlistt[position].quantity + " Bottle Return"
    }

    override fun getItemCount(): Int {
        return returnlistt.size
    }
}