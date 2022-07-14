package com.waterdelivery.returndata.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waterdelivery.R
import com.waterdelivery.ReturndataResponse
import kotlinx.android.synthetic.main.adapter_return_date.view.*

class RetrunDateAdapter(var context: Context,
                        var date:String,
                        var returnlist: ArrayList<ReturndataResponse.Data>):RecyclerView.Adapter<RetrunDateAdapter.RetrunDateHolder>() {
    class RetrunDateHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetrunDateHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_return_date, parent, false)
        return RetrunDateHolder(view)
    }

    override fun onBindViewHolder(holder: RetrunDateHolder, position: Int) {

        if (date.equals(returnlist[position].date)){
            holder.itemView.txt_date.text=returnlist[position].date
            holder.itemView.recy_return.layoutManager=LinearLayoutManager(context)
            holder.itemView.recy_return.adapter=ReturnAdapter(context,date,
                returnlist[position].returnCustomerList as ArrayList<ReturndataResponse.Data.ReturnCustomer>)
        }else{

        }

    }

    override fun getItemCount(): Int {
      return returnlist.size
    }
}