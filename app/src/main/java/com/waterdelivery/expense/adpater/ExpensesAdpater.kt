package com.waterdelivery.expense.adpater

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waterdelivery.R
import com.waterdelivery.expense.activity.ExpenseImageActivity
import com.waterdelivery.expense.modal.ExpensesListResponce
import kotlinx.android.synthetic.main.express_layout.view.*
import java.text.SimpleDateFormat

class ExpensesAdpater(
        var context: Context,
        var expensesList:ArrayList<ExpensesListResponce.Data>
):RecyclerView.Adapter<ExpensesAdpater.ExpensesHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesHolder {
       var view=LayoutInflater.from(parent.context).inflate(R.layout.express_layout,parent,false)
        return  ExpensesHolder(view)
    }

    override fun onBindViewHolder(holder: ExpensesHolder, position: Int) {
        var list=expensesList[position]
        holder.itemView.txt_Exprss_type.text=list.type
        holder.itemView.expn_amount.text="AED " +list.amount
        holder.itemView.txt_expr_cmnt.text=list.comment
        var dates=list.created_date
        holder.itemView.txt_express_date.text = dates
        holder.itemView.exp_image.setOnClickListener {
            context.startActivity(Intent(context,ExpenseImageActivity::class.java).putExtra(
                "image",list.image
            ))
        }

      //  var formatter =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dates)
      //  holder.itemView.txt_express_date.text = SimpleDateFormat("MMM-dd-yy | HH:mm").format(formatter)

    }

    override fun getItemCount(): Int {
        return expensesList.size
    }

    class ExpensesHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}