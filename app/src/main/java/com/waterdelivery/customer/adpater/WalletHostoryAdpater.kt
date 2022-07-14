package com.waterdelivery.customer.adpater

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waterdelivery.R
import com.waterdelivery.customer.modal.WalletHistoryResponce
import kotlinx.android.synthetic.main.history_layout.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WalletHostoryAdpater(
        var context: Context,
        var walletList: ArrayList<WalletHistoryResponce.Data>
):RecyclerView.Adapter<WalletHostoryAdpater.WalletHostoryHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletHostoryHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.history_layout, parent, false)
        return WalletHostoryHolder(view)
    }

    override fun onBindViewHolder(holder: WalletHostoryHolder, position: Int) {
        var list=walletList[position]
        holder.itemView.wallet_name.text=list.name
        if (!list.address.equals("")){
            holder.itemView.wallet_address.text=list.address
        }else{
            holder.itemView.wallet_address.visibility=View.GONE
        }

        holder.itemView.txt_wallet_amount.text="AED " +list.amount
        var dates=list.created_date
        var formatter =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dates)
        holder.itemView.txt_Date.text = SimpleDateFormat("MMM-dd-yy | HH:mm").format(formatter)




    }

    override fun getItemCount(): Int {
     return walletList.size
    }

    class WalletHostoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}