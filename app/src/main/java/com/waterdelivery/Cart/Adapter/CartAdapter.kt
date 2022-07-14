package com.waterdelivery.Cart.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.Cart.AddtoCartResponse
import com.waterdelivery.Cart.CartActivity
import com.waterdelivery.Cart.CartListResponse
import com.waterdelivery.R
import com.waterdelivery.Shop.ProductListResponse
import com.waterdelivery.Shop.ShopActivity
import com.waterdelivery.task.adpater.DateAdapter
import com.waterdelivery.user.LoginActivity
import kotlinx.android.synthetic.main.adapter_cart.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.util.HashMap

class CartAdapter(var context: Context,var cartlist:ArrayList<CartListResponse.Data>,var imageView: ImageView,var button: TextView):RecyclerView.Adapter<CartAdapter.CartHolder>() {
    lateinit var sharprf:shareprefrences
    lateinit var count_text:String
   inner class CartHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    init {
      itemView.tvAdd.setOnClickListener {
          addtocard(adapterPosition,itemView.tvCount,"add")


      }
        itemView.tvMinus.setOnClickListener {
            if(itemView.tvCount.text.toString().toInt()!=1){
                addtocard(adapterPosition,itemView.tvCount,"delete")
            }

        }
        itemView.imvEdit.setOnClickListener {
            addtocard(adapterPosition,itemView.tvCount,"remove")
        }
    }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_cart, parent, false)
        return CartHolder(view)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartHolder, position: Int) {
        sharprf= shareprefrences(context)
        holder.itemView.tvItemName.text=cartlist[position].product_name
        holder.itemView.tvPrice.text="AED " +cartlist[position].price
        holder.itemView.tvCount.text=cartlist[position].cart_quantity.toString()
    }

    override fun getItemCount(): Int {
        return cartlist.size
    }

    fun addtocard(position: Int, textView: TextView,event:String) {


        if (event.equals("add")){
            val count: Int = textView.text.toString().toInt()
            val max_count: Int =cartlist[position].max_quantity.toString().toInt()
            if (count+1>max_count){
                Toast.makeText(context,"Not More Than "+cartlist[position].max_quantity,Toast.LENGTH_SHORT).show()
            }else{

                (context as CartActivity).showProgressDialog()
                val stringStringHashMap = HashMap<String, String>()
                stringStringHashMap.put("email", "")
                var signin: Call<AddtoCartResponse> = APIUtils.getServiceAPI()!!.addtocard(
                    "Bearer " + sharprf.getStringPreference("token"),
                    sharprf.getStringPreference("customer_id").toString(),
                    cartlist[position].product_id,
                    cartlist[position].price,
                    "1",
                    event


                )
                signin.enqueue(object : Callback<AddtoCartResponse> {
                    override fun onResponse(
                        call: Call<AddtoCartResponse>,
                        response: Response<AddtoCartResponse>
                    ) {

                        try {

                            if (response.code() == 200) {
                                (context as CartActivity).hideProgressDialog()

                                if (response.body()!!.status == true) {
                                    if(event=="add"){
                                        toastsucess(response!!.body()!!.message)
                                        textView.text=(textView.text.toString().toInt()+1).toString()
                                    }else if(event=="delete"){
                                        toastsucess(response!!.body()!!.message)
                                        textView.text=(textView.text.toString().toInt()-1).toString()
                                    }else if(event=="remove"){
                                        toastsucess(response!!.body()!!.message)
                                        cartlist.removeAt(position)
                                        notifyDataSetChanged()
                                        if(cartlist.size>0){
                                            imageView.visibility=View.GONE
                                            button.visibility=View.VISIBLE
                                        }else{
                                            imageView.visibility=View.VISIBLE
                                            button.visibility=View.GONE
                                        }
                                    }

                                } else {
                                    toasterror(response.body()!!.message)
                                }

                            } else if (response.code() == 401) {
                                sharprf.clearAllPreferences()
                                (context as CartActivity).startActivity(Intent(context, LoginActivity::class.java))
                                (context as CartActivity).finishAffinity()


                            } else if (response.code() == 400) {
                                (context as CartActivity).hideProgressDialog()
                                (context as CartActivity).showToastMessage(
                                    context,
                                    response.body()!!.message
                                )

                            } else if (response.code() == 500) {
                                (context as CartActivity).hideProgressDialog()
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

        }else{
            (context as CartActivity).showProgressDialog()
            val stringStringHashMap = HashMap<String, String>()
            stringStringHashMap.put("email", "")
            var signin: Call<AddtoCartResponse> = APIUtils.getServiceAPI()!!.addtocard(
                "Bearer " + sharprf.getStringPreference("token"),
                sharprf.getStringPreference("customer_id").toString(),
                cartlist[position].product_id,
                cartlist[position].price,
                "1",
                event


            )
            signin.enqueue(object : Callback<AddtoCartResponse> {
                override fun onResponse(
                    call: Call<AddtoCartResponse>,
                    response: Response<AddtoCartResponse>
                ) {

                    try {

                        if (response.code() == 200) {
                            (context as CartActivity).hideProgressDialog()

                            if (response.body()!!.status == true) {
                                if(event=="add"){
                                    toastsucess(response!!.body()!!.message)
                                    textView.text=(textView.text.toString().toInt()+1).toString()
                                }else if(event=="delete"){
                                    toastsucess(response!!.body()!!.message)
                                    textView.text=(textView.text.toString().toInt()-1).toString()
                                }else if(event=="remove"){
                                    toastsucess(response!!.body()!!.message)
                                    cartlist.removeAt(position)
                                    notifyDataSetChanged()
                                    if(cartlist.size>0){
                                        imageView.visibility=View.GONE
                                        button.visibility=View.VISIBLE
                                    }else{
                                        imageView.visibility=View.VISIBLE
                                        button.visibility=View.GONE
                                    }
                                }

                            } else {
                                toasterror(response.body()!!.message)
                            }

                        } else if (response.code() == 401) {
                            sharprf.clearAllPreferences()
                            (context as CartActivity).startActivity(Intent(context, LoginActivity::class.java))
                            (context as CartActivity).finishAffinity()


                        } else if (response.code() == 400) {
                            (context as CartActivity).hideProgressDialog()
                            (context as CartActivity).showToastMessage(
                                context,
                                response.body()!!.message
                            )

                        } else if (response.code() == 500) {
                            (context as CartActivity).hideProgressDialog()
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