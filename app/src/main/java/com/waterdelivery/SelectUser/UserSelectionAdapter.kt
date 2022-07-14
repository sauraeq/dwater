package com.waterdelivery.SelectUser

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.waterdelivery.R
import com.waterdelivery.Shop.ShopActivity
import com.waterdelivery.customer.fragement.model.SearchResponse
import kotlinx.android.synthetic.main.customer_list_layout.view.*

class UserSelectionAdapter(
    var context: Context,
    var list: List<SearchResponse.Data>
):RecyclerView.Adapter<UserSelectionAdapter.UserSelectionHolder>() {
    lateinit var sharprf: shareprefrences;
 inner   class UserSelectionHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
init {
    itemView.userselection.setOnClickListener {
        sharprf.setStringPreference("selecetshopcutomer",list[adapterPosition].id)
        context.startActivity(Intent(context, ShopActivity::class.java))

    }
}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSelectionHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.customer_list_layout,parent,false)
        return UserSelectionHolder(view)
    }

    override fun onBindViewHolder(holder: UserSelectionHolder, position: Int) {
        sharprf= shareprefrences(context)
        holder.itemView.tvname.text=list[position].name
        holder.itemView.txt_email.text=list[position].email
        holder.itemView.phonenumber.text=list[position].mobile_number
        holder.itemView.wallet_ammount.text="AED "  + list[position].wallet_amount

        Glide.with(context).load(list[position].profile).into(holder.itemView.expense_men)
    }


    override fun getItemCount(): Int {
       return  list.size
    }
}