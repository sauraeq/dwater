package com.waterdelivery.orderdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.waterdelivery.R
import com.waterdelivery.task.modal.TaskListResponse
import kotlinx.android.synthetic.main.taskdetails_listview_layout.view.*

class TaskDeatilsAdapter(
    var context: Context,
    var productlist: ArrayList<TaskListResponse.Data.Order.Product>
) : RecyclerView.Adapter<TaskDeatilsAdapter.TaskDeatilsHolder>() {
    class TaskDeatilsHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskDeatilsHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.taskdetails_listview_layout, parent, false)
        return TaskDeatilsHolder(view)
    }

    override fun onBindViewHolder(holder: TaskDeatilsHolder, position: Int) {
        holder.itemView.productname.text = productlist[position].product_name
        holder.itemView.water.text = "QTY " + productlist[position].quantity
        Glide.with(context).load(productlist[position].product_image)
            .into(holder.itemView.image_bottol)
    }

    override fun getItemCount(): Int {
        return productlist.size
    }
}