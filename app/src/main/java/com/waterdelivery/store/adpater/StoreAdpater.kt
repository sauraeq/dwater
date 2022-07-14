package com.waterdelivery.store.adpater

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.R
import com.waterdelivery.main.DashBoardActivity
import com.waterdelivery.orderdelivery.OrderDeliveryActivity
import com.waterdelivery.store.StockInterface
import com.waterdelivery.store.model.StoreAcceptResponse
import com.waterdelivery.store.model.StoreListResponse
import com.waterdelivery.user.LoginActivity
import kotlinx.android.synthetic.main.store.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast

class StoreAdpater(
    var context: Context,
    var list: ArrayList<StoreListResponse.Data>,
    val lintener: StockInterface,
    var errordata: ImageView,
    var driverdata: StoreListResponse.DriverDetails
) : RecyclerView.Adapter<StoreAdpater.StoreHolder>() {
    lateinit var sharprf: shareprefrences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.store, parent, false)
        return StoreHolder(view)
    }

    override fun onBindViewHolder(holder: StoreHolder, position: Int) {
        sharprf = shareprefrences(context)
        /*Glide.with(context).load(list[position].image).into(holder.itemView.imagepictire)
        holder.itemView.textView.text = list[position].store_name
        holder.itemView.textView2.text = "AED " + list[position].base_price
        holder.itemView.textView3.text = "QTY " + list[position].quanity
        if (list[position].driver_acceptance== "Yes") {
            holder.itemView.txt_accpet.text = "ACCEPTED"
            holder.itemView.viewwww.visibility=View.GONE
            holder.itemView.dobule.visibility=View.GONE
            holder.itemView.txt_accpet.setTypeface(holder.itemView.txt_accpet.getTypeface(), Typeface.BOLD);
        }else if(list[position].driver_acceptance == "No"){
            holder.itemView.txt_accpet.text = "Reject"
            holder.itemView.viewwww.visibility=View.GONE
            holder.itemView.dobule.visibility=View.GONE
            holder.itemView.txt_accpet.setTypeface(holder.itemView.txt_accpet.getTypeface(), Typeface.BOLD);
        }*/

        holder.itemView.item_name.text = list[position].store_name
        holder.itemView.current_stock.text = list[position].quantity
        holder.itemView.new_stock.text = list[position].new_quantity

        if (list[position].driver_acceptance== "Yes") {
            holder.itemView.accept.text = "ACCEPTED"
            holder.itemView.accepted.visibility=View.VISIBLE
            holder.itemView.accept.visibility=View.GONE
           // holder.itemView.viewwww.visibility=View.GONE
           // holder.itemView.dobule.visibility=View.GONE
            //holder.itemView.txt_accpet.setTypeface(holder.itemView.txt_accpet.getTypeface(), Typeface.BOLD);
        }else if(list[position].driver_acceptance == "No"){
            holder.itemView.accept.text = "Reject"
            holder.itemView.accepted.visibility=View.VISIBLE
            holder.itemView.accepted.text = "Rejected"
            holder.itemView.accept.visibility=View.GONE
           // holder.itemView.viewwww.visibility=View.GONE
          //  holder.itemView.dobule.visibility=View.GONE
           // holder.itemView.txt_accpet.setTypeface(holder.itemView.txt_accpet.getTypeface(), Typeface.BOLD);
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class StoreHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {

            itemView.accept.setOnClickListener {
                if (list[adapterPosition].driver_acceptance != "Yes") {
                    opendialog(adapterPosition)
//                storeaccept(list[adapterPosition].order_id,list[adapterPosition].quantity,position)
                }
            }

            itemView.constrain_grid.setOnClickListener{
                if (list[adapterPosition].driver_acceptance == "Yes") {
                   /* var intent = Intent(context, OrderDeliveryActivity::class.java)
                    intent.putExtra("driverdata", driverdata)
                    intent.putExtra("bottledata", list[adapterPosition])
                    context.startActivity(intent)*/
//                storeaccept(list[adapterPosition].order_id,list[adapterPosition].quantity,position)
                }
            }

        }
    }

    private fun opendialog(position: Int) {
        val dialog = context.let { Dialog(it, R.style.DialogCustomTheme) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //  dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_acppertreject)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.findViewById<TextView>(R.id.diolog_accept).setOnClickListener {
            storeaccept(
                list[position].id.toString(),
                dialog.findViewById<EditText>(R.id.comment).text.toString(),
                "Yes",
                position,
                dialog
            )
        }
        dialog.findViewById<TextView>(R.id.diolog_reject).setOnClickListener {
            if (dialog.findViewById<EditText>(R.id.comment).text.toString().trim().isEmpty()) {
                toasterror("Please Enter Comemnt")
            } else {
                storeaccept(
                    list[position].id.toString(),
                    dialog.findViewById<EditText>(R.id.comment).text.toString(),
                    "No",
                    position,
                    dialog
                )
            }
        }
        dialog.show()
    }

    fun storeaccept(
        storeid: String,
        comment: String,
        driveraxppet: String,
        position: Int,
        dialog: Dialog
    ) {
        (context as DashBoardActivity).showProgressDialog()
//        val stringStringHashMap = HashMap<String, String>()
//        stringStringHashMap.put("customer_id", sharprf.getStringPreference("customer_id").toString())
//        stringStringHashMap.put("order_id", oderid)
//        stringStringHashMap.put("total_delivered_can", quantity)
        var signin: Call<StoreAcceptResponse> = APIUtils.getServiceAPI()!!.driverStoreAccept(
            "Bearer " + sharprf.getStringPreference("token"),
            sharprf.getStringPreference("customer_id").toString(),
            storeid,
            driveraxppet,
            comment,

            )
        signin.enqueue(object : Callback<StoreAcceptResponse> {
            override fun onResponse(
                call: Call<StoreAcceptResponse>,
                response: Response<StoreAcceptResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        (context as DashBoardActivity).hideProgressDialog()
                        if (response.body()!!.status == true) {
                            dialog.dismiss()
//                            list.removeAt(position)
                            toastsucess(response.body()!!.message)
                            if (list.size > 0) {
                                errordata.visibility = View.GONE
                            } else {
                                errordata.visibility = View.VISIBLE
                            }
                            list[position].driver_acceptance=driveraxppet
                            notifyDataSetChanged()
                            lintener.onClick()

                        } else {
                            toasterror(response.body()!!.message)
                        }

                    } else if (response.code() == 401) {

                        sharprf.clearAllPreferences()
                        context.startActivity(Intent(context, LoginActivity::class.java))
                        (context as DashBoardActivity).finishAffinity()


                    } else if (response.code() == 400) {
                        (context as DashBoardActivity).hideProgressDialog()
                        (context as DashBoardActivity).showToastMessage(
                            context,
                            response.body()!!.message
                        )

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<StoreAcceptResponse>, t: Throwable) {
                (context as DashBoardActivity).hideProgressDialog()
            }

        })
    }

    fun toastsucess(msg: String) {
        MotionToast.darkToast(
            context as Activity,
            "Hurray success 😍",
            msg,
            MotionToast.TOAST_SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(context, R.font.helvetica_regular)
        )
    }

    fun toasterror(msg: String) {
        MotionToast.darkToast(
            context as Activity,
            "Sorry ☹ ",
            msg,
            MotionToast.TOAST_ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(context, R.font.helvetica_regular)
        )
    }
}