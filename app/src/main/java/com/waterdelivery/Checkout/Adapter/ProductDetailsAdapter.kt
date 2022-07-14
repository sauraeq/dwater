package com.waterdelivery.Checkout.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.waterdelivery.Checkout.PlaceOrderResposne
import com.waterdelivery.R
import com.waterdelivery.task.adpater.DateAdapter
import kotlinx.android.synthetic.main.adapter_checkout.view.*
import kotlinx.android.synthetic.main.adapter_checkout1.view.*

class ProductDetailsAdapter(var context: Context, var productlist:PlaceOrderResposne.Data.Result.Productlist):RecyclerView.Adapter<ProductDetailsAdapter.CheckoutHolder>() {
    class CheckoutHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductDetailsAdapter.CheckoutHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_checkout1, parent, false)
        return CheckoutHolder(view)
    }

    override fun onBindViewHolder(holder: ProductDetailsAdapter.CheckoutHolder, position: Int) {
        holder.itemView.prod_name.text=productlist.product_name
        holder.itemView.qty.text="QTY "+ productlist.quantity
        holder.itemView.price.text="AED "+ productlist.amount
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}