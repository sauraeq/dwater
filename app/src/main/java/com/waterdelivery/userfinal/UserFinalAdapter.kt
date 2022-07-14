package com.waterdelivery.userfinal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waterdelivery.R
import kotlinx.android.synthetic.main.adapter_finaluser.view.*

class UserFinalAdapter(var context: Context,var list:ActivityRespons.Data.DriverActivity) :
    RecyclerView.Adapter<UserFinalAdapter.UserFinalHolder>() {
    class UserFinalHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFinalHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_finaluser, parent, false)
        return UserFinalHolder(view)
    }

    override fun onBindViewHolder(holder: UserFinalHolder, position: Int) {
        holder.itemView.totaltopup.text="AED " +list.total_topup
        holder.itemView.totalexpense.text="AED " +list.total_expense
        holder.itemView.totalsale.text="AED " +list.total_cash_sale
        holder.itemView.totalearning.text="AED " +list.total_earning
        holder.itemView.totala.text="AED " +list.final_net_available
        holder.itemView.credit_sale.text="AED " +list.credit_sale

        holder.itemView.cash_sale.text="AED " +list.cash_sale
        holder.itemView.wallet_sale.text="AED " +list.wallet_sale


    }

    override fun getItemCount(): Int {
        return 1
    }
}