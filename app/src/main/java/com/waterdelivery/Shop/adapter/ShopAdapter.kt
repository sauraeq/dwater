package com.waterdelivery.Shop.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.Cart.AddtoCartResponse
import com.waterdelivery.Cart.CartActivity
import com.waterdelivery.R
import com.waterdelivery.Shop.ProductListResponse
import com.waterdelivery.Shop.ShopActivity
import com.waterdelivery.customer.fragement.model.SearchResponse
import com.waterdelivery.task.adpater.DateAdapter
import com.waterdelivery.user.LoginActivity
import kotlinx.android.synthetic.main.activity_add_retrun.*
import kotlinx.android.synthetic.main.adapter_shop.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.util.HashMap

class ShopAdapter(var context:Context,var list:ArrayList<ProductListResponse.Data>):RecyclerView.Adapter<ShopAdapter.ShopHolder>() {
    lateinit var sharprf:shareprefrences
  inner  class ShopHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
init {
    itemView.addtoard.setOnClickListener {
        if(itemView.addtoard.text=="View Cart"){
             context.startActivity(Intent(context,CartActivity::class.java))
        }else{
            addtocard(list[position],itemView.addtoard)
        }

    }
}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_shop, parent, false)
        return ShopHolder(view)
    }

    override fun onBindViewHolder(holder: ShopHolder, position: Int) {
        sharprf= shareprefrences(context)
        Glide.with(context).load(list[position].image).into(holder.itemView.product_iumage);
        holder.itemView.product_descrition.text=list[position].product_name
        holder.itemView.product_price.text="AED " + list[position].selling_price
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addtocard(data:ProductListResponse.Data,textView: TextView) {
        (context as ShopActivity).showProgressDialog()
        val stringStringHashMap = HashMap<String, String>()
        stringStringHashMap.put("email", "")
        var signin: Call<AddtoCartResponse> = APIUtils.getServiceAPI()!!.addtocard(
            "Bearer " + sharprf.getStringPreference("token"),
            sharprf.getStringPreference("customer_id").toString(),
            data.product_id,
            data.selling_price,
            "1",
            "add"


        )
        signin.enqueue(object : Callback<AddtoCartResponse> {
            override fun onResponse(
                call: Call<AddtoCartResponse>,
                response: Response<AddtoCartResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        (context as ShopActivity).hideProgressDialog()

                        if (response.body()!!.status == true) {
                            toastsucess(response.body()!!.message)
                            textView.text="View Cart"
                            (context as ShopActivity).count()
                        } else {
                            toasterror(response.body()!!.message)
                        }

                    } else if (response.code() == 401) {
                        sharprf.clearAllPreferences()
                        (context as ShopActivity).startActivity(Intent(context, LoginActivity::class.java))
                        (context as ShopActivity).finishAffinity()


                    } else if (response.code() == 400) {
                        (context as ShopActivity).hideProgressDialog()
                        (context as ShopActivity).showToastMessage(
                            context,
                            response.body()!!.message
                        )

                    } else if (response.code() == 500) {
                        (context as ShopActivity).hideProgressDialog()
//                        mainacivity.showToastMessage(context,response!!.body()!!.message)

                    }

                } catch (e: Exception) {


                }

            }

            override fun onFailure(call: Call<AddtoCartResponse>, t: Throwable) {
                (context as ShopActivity).hideProgressDialog()
            }

        })
    }

    fun toasterror(msg: String) {
        MotionToast.darkToast(
            context as Activity,
            "Sorry ☹ ",
            msg,
            MotionToast.TOAST_ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(context as Activity, R.font.helvetica_regular)
        )
    }

    fun toastsucess(msg: String) {
        this?.let {
            MotionToast.darkToast(
                context as Activity,
                "Hurray success 😍",
                msg,
                MotionToast.TOAST_SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(context as Activity, R.font.helvetica_regular)
            )
        }
    }
}