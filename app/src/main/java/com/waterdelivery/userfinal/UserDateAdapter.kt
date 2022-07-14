package com.waterdelivery.userfinal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waterdelivery.R
import com.waterdelivery.task.adpater.DateAdapter
import kotlinx.android.synthetic.main.rec_item_wallet.view.*

class UserDateAdapter(var context: Context,var list:ArrayList<ActivityRespons.Data>):RecyclerView.Adapter<UserDateAdapter.UserHolder>() {
    class UserHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rec_item_wallet, parent, false)
        return UserHolder(view)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.itemView.txt_month.text=list[position].current_date
holder.itemView.recy_credited_subdata.layoutManager=LinearLayoutManager(context)
holder.itemView.recy_credited_subdata.adapter=UserFinalAdapter(context,list[position].driver_activity)

    }

    override fun getItemCount(): Int {
       return list.size
    }
}