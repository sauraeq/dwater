package com.waterdelivery.customer.adpater

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.R
import com.waterdelivery.Shop.ShopActivity
import com.waterdelivery.customer.fragement.model.AddmoneyResponse
import com.waterdelivery.customer.fragement.model.SearchResponse
import com.waterdelivery.main.DashBoardActivity
import com.waterdelivery.receipt.ReceiptActivity
import com.waterdelivery.user.LoginActivity
import kotlinx.android.synthetic.main.customer_list_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.util.HashMap

class ListCustomerAdpater(
    var context: Context,
    var list: List<SearchResponse.Data>
):RecyclerView.Adapter<ListCustomerAdpater.ListCustomerHolder>(){
    var longitude: String = ""
    var langitude: String = ""
    lateinit var sharprf: shareprefrences


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCustomerHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.customer_list_layout,parent,false)
        return ListCustomerHolder(view)
    }

    override fun onBindViewHolder(holder: ListCustomerHolder, position: Int) {
        getCurrentlocation()
sharprf= shareprefrences(context)
holder.itemView.tvname.text=list[position].name
holder.itemView.txt_email.text=list[position].email
holder.itemView.phonenumber.text=list[position].mobile_number
        holder.itemView.wallet_ammount.text="AED "  + list[position].wallet_amount
        holder.itemView.txt_Add_money.setOnClickListener {
            opendialog(list[position].id,list[position].name,list[position].mobile_number)
        }
        holder.itemView.userselection.setOnClickListener {
            opendialogforchosse(list[position].id,list[position].name,list[position].mobile_number)
        }
        Glide.with(context).load(list[position].profile).into(holder.itemView.expense_men)
    }

    override fun getItemCount(): Int {
     return list.size
    }

    class ListCustomerHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun opendialog(id:String,name:String,number:String) {
        val dialog = context?.let { Dialog(it, R.style.DialogCustomTheme) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //  dialog.setCancelable(false)

        dialog?.setContentView(R.layout.top_up_layout)
        dialog?.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.setCancelable(false)
        dialog.findViewById<TextView>(R.id.add_money).setOnClickListener {
            if(dialog.findViewById<EditText>(R.id.ammount).text.toString().trim().isEmpty()){
                toasterror("Please Enter Amount")
            }else if(dialog.findViewById<EditText>(R.id.ammount).text.toString().toInt()==0){
                toasterror("Amount should be greater than Zero ")
            }else if(dialog.findViewById<EditText>(R.id.ammount).text.toString().toInt()>1000){
                toasterror("You Cant Enter More Than 1000")
            }
            else{
                apicall(id,dialog.findViewById<EditText>(R.id.ammount).text.toString(),dialog)
            }
        }
        dialog.findViewById<TextView>(R.id.customername).text=name
        dialog.findViewById<TextView>(R.id.customerphone).text=number
        dialog.findViewById<ImageView>(R.id.close).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()


    }



    fun opendialogforchosse(id:String,name:String,number:String) {
        val dialog = context?.let { Dialog(it, R.style.DialogCustomTheme) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //  dialog.setCancelable(false)

        dialog?.setContentView(R.layout.dialog_open)
        dialog?.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.setCancelable(true)
        dialog.findViewById<TextView>(R.id.add_money).setOnClickListener {
            dialog.cancel()
            opendialog(id,name,number)
        }
        dialog.findViewById<TextView>(R.id.shopp).setOnClickListener {
            sharprf.setStringPreference("selecetshopcutomer",id)
            context.startActivity(Intent(context,ShopActivity::class.java))
            dialog.cancel()
        }


        dialog.show()


    }


    fun toastsucess(msg:String) {
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

    fun toasterror(msg:String){

        MotionToast.darkToast(
            context as Activity,
            "Sorry ☹ ",
            msg,
            MotionToast.TOAST_ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(context,R.font.helvetica_regular))

    }

    fun apicall( cusomerid:String,ammount:String,dailog:Dialog) {
        (context as DashBoardActivity).showProgressDialog()
        val stringStringHashMap = HashMap<String, String>()
        val user_id:String=sharprf.getStringPreference((context as DashBoardActivity).USER_ID).toString()
        var lat:String= (context as DashBoardActivity).langitude.toString()
        var long:String=(context as DashBoardActivity).longitude.toString()

        var orderdilivery: Call<AddmoneyResponse> = APIUtils.getServiceAPI()!!.addMoney(
            "Bearer " + sharprf.getStringPreference("token"),
            ammount,
            cusomerid,
            sharprf.getStringPreference((context as DashBoardActivity).USER_ID).toString(),
            (context as DashBoardActivity).langitude.toString(),
            (context as DashBoardActivity).longitude.toString()


            )
        orderdilivery.enqueue(object : Callback<AddmoneyResponse> {
            override fun onResponse(
                call: Call<AddmoneyResponse>,
                response: Response<AddmoneyResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        dailog.dismiss()
                        (context as DashBoardActivity).hideProgressDialog()

                        if (response.body()!!.status == true) {
                            toastsucess(response!!.body()!!.message)
                            var intent=Intent(context,ReceiptActivity::class.java)
                            intent.putExtra("recepitdata",response!!.body()!!.data)
                            context.startActivity(intent)

//                            searchActivity.showToastMessage(context,response!!.body()!!.message)
                        }else if(response.code()==401) {
                            sharprf.clearAllPreferences()
                            (context as DashBoardActivity).startActivity(Intent(context, LoginActivity::class.java))
                            (context as DashBoardActivity).finishAffinity()

                        }
                    }else if(response.code()==400){
                        (context as DashBoardActivity).hideProgressDialog()
                        (context as DashBoardActivity).showToastMessage(context,response!!.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<AddmoneyResponse>, t: Throwable) {
                (context as DashBoardActivity).hideProgressDialog()
            }

        })
    }

    fun getCurrentlocation() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        /* val locationListener: LocationListener = object : LocationListener() {
             fun onLocationChanged(location: Location) {
                 val latitute: Double = location.getLatitude()
                 val longitute: Double = location.getLongitude()
                 Log.e("lati", latitute.toString())
                 Log.e("long", longitute.toString())
             }

             fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
             fun onProviderEnabled(provider: String?) {}
             fun onProviderDisabled(provider: String?) {}
         }*/

        val locationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                longitude = location.longitude.toString()
                langitude = location.latitude.toString()
                Log.e("langitudefff", location.longitude.toString())
                Log.e("fffffff", location.latitude.toString())

            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        try {
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                500,
                0f,
                locationListener
            )
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }
    }


}