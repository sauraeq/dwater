package com.waterdelivery.Checkout.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.waterdelivery.Cart.CartCheckoutResponse
import com.waterdelivery.R
import com.waterdelivery.task.adpater.DateAdapter
import kotlinx.android.synthetic.main.adapter_checkout.view.*

class CheckoutAdapter(var context: Context,var productlist:ArrayList<CartCheckoutResponse.Data.Product>):RecyclerView.Adapter<CheckoutAdapter.CheckoutHolder>() {
    class CheckoutHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheckoutAdapter.CheckoutHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_checkout, parent, false)
        return CheckoutHolder(view)
    }

    override fun onBindViewHolder(holder: CheckoutAdapter.CheckoutHolder, position: Int) {
        Glide.with(context).load(productlist[position].image).into(holder.itemView.imvProduct)
        holder.itemView.tvItemName.text=productlist[position].product_name
        holder.itemView.tvItemName.text=productlist[position].product_name
        holder.itemView.tvGallon.text="QTY "+ productlist[position].cart_quantity
        holder.itemView.tvPrice.text="AED "+ productlist[position].price
    }

    override fun getItemCount(): Int {
        return productlist.size
    }
}