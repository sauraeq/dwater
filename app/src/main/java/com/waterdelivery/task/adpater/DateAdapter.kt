package com.waterdelivery.task.adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waterdelivery.R
import com.waterdelivery.task.modal.TaskListResponse
import kotlinx.android.synthetic.main.rec_item_wallet.view.*

class DateAdapter(var context: Context,var latitude:String,var longitude:String,var location:String, var datalist: ArrayList<TaskListResponse.Data>) :
    RecyclerView.Adapter<DateAdapter.DateHolder>() {
    class DateHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rec_item_wallet, parent, false)
        return DateHolder(view)
    }

    override fun onBindViewHolder(holder: DateHolder, position: Int) {
        holder.itemView.txt_month.text = datalist[position].current_date
        holder.itemView.recy_credited_subdata.layoutManager = LinearLayoutManager(context)
        holder.itemView.recy_credited_subdata.adapter = TaskListAdpater(context,latitude,longitude,location,
            datalist[position].Order_List as ArrayList<TaskListResponse.Data.Order>
        )
    }

    override fun getItemCount(): Int {
        return datalist.size
    }
}