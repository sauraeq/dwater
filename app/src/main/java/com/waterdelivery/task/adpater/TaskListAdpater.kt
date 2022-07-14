package com.waterdelivery.task.adpater

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.R
import com.waterdelivery.customer.fragement.model.AddmoneyResponse
import com.waterdelivery.main.DashBoardActivity
import com.waterdelivery.orderdetails.TaskDetailsActivity
import com.waterdelivery.task.modal.TaskListResponse
import com.waterdelivery.user.LoginActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.task_listview_layout.view.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class TaskListAdpater(var context: Context,var latitude:String,var longitude:String,var delivered_address:String, var leest: ArrayList<TaskListResponse.Data.Order>) :
    RecyclerView.Adapter<TaskListAdpater.TaskHolder>() {
    private var requestQueue: RequestQueue? = null
    lateinit var sharprf: shareprefrences
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_listview_layout, parent, false)
        return TaskHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        sharprf = shareprefrences(context)
        if ((leest[position].address) != null) {
            if (leest[position].address.first_name.isNotEmpty()) {
//                 holder.itemView.list_photo.visibility=View.VISIBLE
//                holder.itemView.list_photo.text =
//                    leest[position].address.first_name + " " + leest[position].address.last_name
            }
            holder.itemView.location.text = leest[position].address.city
        }

        holder.itemView.descrion.text = leest[position].name


        if (leest[position].address != null) {
            holder.itemView.water.text = leest[position]!!.address!!.phone_no
            holder.itemView.list_map.text =
                leest[position].address.address+ " " + leest[position].address.city + ", " + leest[position].address.district
        }

        holder.itemView.list_water.text = leest[position].quantity + " Water Bottles"

        holder.itemView.price.text = "AED " + leest[position].total_amt
        holder.itemView.status.text = leest[position].delever_status
        if (leest[position].delever_status == "Cancelled") {
            holder.itemView.status.setTextColor(Color.parseColor("#FF0000"))
        } else if (leest[position].delever_status == "Pending") {
            holder.itemView.status.setTextColor(Color.parseColor("#8B8000"))
        }
        Glide.with(context).load(leest[position].profile).into(holder.itemView.image_bottol)
        holder.itemView.water.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data =
                Uri.parse("tel:" + leest[position].address.phone_no) //change the number
            context.startActivity(callIntent)
        }
        holder.itemView.totaldata.setOnClickListener {
            var intent = Intent(context, TaskDetailsActivity::class.java)
            intent.putExtra("taskdata", leest[position])
            context.startActivity(intent)
        }
        holder.itemView.orderid.text = "Order ID " + leest[position].order_id
    }

    override fun getItemCount(): Int {
        return leest.size
    }

    inner class TaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.status.setOnClickListener {
                if (leest[adapterPosition].delever_status == "Pending") {
                    opendialog(adapterPosition)
                }
            }
            itemView.list_map.setOnClickListener {
                try {

                    if (leest[adapterPosition].latitude.equals(null)){
                        Toast.makeText(context,"No Location",Toast.LENGTH_SHORT).show()
                    }else{
                        var geoUri =
                            "http://maps.google.com/maps?q=loc:" + leest[adapterPosition].latitude + "," + leest[adapterPosition].longitude + " (" + leest[adapterPosition].address.location_address + ")"
                        var mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
                        if (mapIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(mapIntent)
                        }
                    }


                }catch (e:Exception){
                    Toast.makeText(context,"No Location",Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun opendialog(position: Int) {
        val dialog = context.let { Dialog(it, R.style.DialogCustomTheme) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_status)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.findViewById<RelativeLayout>(R.id.text_delivered).setOnClickListener {
           /* var order_id:String = leest[position].order_id
            var driver_id:String=sharprf.getStringPreference("customer_id").toString()
            var token : String="Bearer " + sharprf.getStringPreference("token")
            data(token,order_id,driver_id,"Delivered",latitude,longitude,delivered_address)*/
            apicall("Delivered", position, dialog)
            //data(position,"Delivered" )
        }
        dialog.findViewById<RelativeLayout>(R.id.pending).setOnClickListener {
            apicall("Pending", position, dialog)
        }
        dialog.findViewById<RelativeLayout>(R.id.cancel).setOnClickListener {
            apicall("Cancel", position, dialog)
        }
        dialog.show()

    }

    fun data(position: Int,status: String){

        val queue = AppController.getInstance()

        val tag_json_obj = "json_obj_req"

        val url = "http://demo.equalinfotech.com/Waterdelivery/api/Driver/orderTaskListUpdate"



        val jsonObjReq: JsonObjectRequest = object : JsonObjectRequest(Method.POST,
            url, null,
            object : com.android.volley.Response.Listener<JSONObject?> {


                override fun onResponse(response: JSONObject?) {

                    Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show()
                    TODO("Not yet implemented")
                }
            }, object : com.android.volley.Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    //VolleyLog.d(TAG, "Error: " + error.message)
                    Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show()
                }
            }) {
            /**
             * Passing some request headers
             */
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authapikey"] = "Bearer " + sharprf.getStringPreference("token")
                return headers
            }

            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                var order_id:String = leest[position].order_id
                var driver_id:String=sharprf.getStringPreference("customer_id").toString()
                var st_status:String=status
                params["order_id"] = order_id
                params["driver_id"] = driver_id
                params["order_status"] = st_status
                params["latitude"] = latitude
                params["longitude"] = longitude
                params["delivered_address"] = delivered_address
                return params
            }
        }

// Adding request to request queue
        queue.addToRequestQueue(jsonObjReq,
            tag_json_obj);

// Adding request to request queue
        queue.getRequestQueue().cancelAll(tag_json_obj);
    }
    fun apicall(status: String, position: Int, dailog: Dialog) {
        (context as DashBoardActivity).showProgressDialog()
        val stringStringHashMap = HashMap<String, String>()
        var token:String="Bearer " + sharprf.getStringPreference("token")
        var driver_id:String=sharprf.getStringPreference("customer_id").toString()
        var orderdilivery: Call<TaskListUpdate> = APIUtils.getServiceAPI()!!.orderTaskListUpdate(
            "Bearer " + sharprf.getStringPreference("token"),
            leest[position].order_id,
            driver_id,
            status,
            latitude,
            longitude,
            "No"
        )
        orderdilivery.enqueue(object : Callback<TaskListUpdate> {
            override fun onResponse(
                call: Call<TaskListUpdate>,
                response: Response<TaskListUpdate>,
            ) {

                try {

                    if (response.code() == 200) {
                        dailog.dismiss()
                        (context as DashBoardActivity).hideProgressDialog()
                        if (response.body()!!.status == true) {
                            toastsucess(response.body()!!.message)
                            leest[position].delever_status = status
                            notifyDataSetChanged()
                            dailog.dismiss()
                        } else if (response.code() == 401) {
                            sharprf.clearAllPreferences()
                            (context as DashBoardActivity).startActivity(
                                Intent(
                                    context,
                                    LoginActivity::class.java
                                )
                            )
                            (context as DashBoardActivity).finishAffinity()

                        }
                    } else if (response.code() == 400) {
                        (context as DashBoardActivity).hideProgressDialog()
                        (context as DashBoardActivity).showToastMessage(
                            context,
                            "No Product Awailable"
                        )
                        (context as DashBoardActivity).showToastMessage(
                            context,
                            response.body()!!.message
                        )
                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<TaskListUpdate>, t: Throwable) {
                (context as DashBoardActivity).hideProgressDialog()
                (context as DashBoardActivity).showToastMessage(
                    context,
                    "No Product Awailable"
                )
            }
        })
    }

/*
    fun apicall(status: String, position: Int, dailog: Dialog) {
        (context as DashBoardActivity).showProgressDialog()
        var order_id:String = leest[position].order_id
        var driver_id:String=sharprf.getStringPreference("customer_id").toString()
        var st_status:String=status
        var token : String="Bearer "+sharprf.getStringPreference("token")

        val stringStringHashMap = java.util.HashMap<String, String>()
        stringStringHashMap.put("order_id", order_id)
        stringStringHashMap.put("driver_id", driver_id)
        stringStringHashMap.put("order_status", status)


        var orderdilivery: Call<TaskUpdate> = APIUtils.getServiceAPI()!!.orderTaskListUpdate(
            token,
            stringStringHashMap
        )
        orderdilivery.enqueue(object : Callback<TaskUpdate> {
            override fun onResponse(
                call: Call<TaskUpdate>,
                response: Response<TaskUpdate>,
            ) {

                try {

                    if (response.code() == 200) {
                        dailog.dismiss()
                        (context as DashBoardActivity).hideProgressDialog()
                        if (response.body()!!.status == true) {
                            toastsucess(response.body()!!.message)
                            leest[position].delever_status = status
                            notifyDataSetChanged()
                            dailog.dismiss()
                        }

                        else if (response.code() == 401) {
                            toastsucess(response.body()!!.message)
                            sharprf.clearAllPreferences()
                            (context as DashBoardActivity).startActivity(
                                Intent(
                                    context,
                                    LoginActivity::class.java
                                )
                            )
                            (context as DashBoardActivity).finishAffinity()

                        }
                    } else if (response.code() == 400) {
                        (context as DashBoardActivity).hideProgressDialog()
                        (context as DashBoardActivity).showToastMessage(
                            context,"Please add product quantity"
                        )
                        (context as DashBoardActivity).showToastMessage(
                            context,
                            response.body()!!.message
                        )
                    }

                } catch (e: Exception) {
                   // toastsucess(response.body()!!.message)
                }

            }

            override fun onFailure(call: Call<TaskUpdate>, t: Throwable) {
                (context as DashBoardActivity).hideProgressDialog()
            }
        })
    }
*/

    fun data(token:String,
             order_id:String,
             driver_id:String,
             st_status:String,
             latitude:String,
             longitude:String,
             delivered_address:String){

        val url = "http://demo.equalinfotech.com/Waterdelivery/api/Driver/orderTaskListUpdate"


        val queue = Volley.newRequestQueue(context)

        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            com.android.volley.Response.Listener { response ->

                try {

                    val respObj = JSONObject(response)

                    val name = respObj.getString("name")
                    val job = respObj.getString("job")

                    // on below line we are setting this string s to our text view.
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, com.android.volley.Response.ErrorListener { error -> // method to handle errors.
                Toast.makeText(
                    context,
                    "Fail to get response = $error",
                    Toast.LENGTH_SHORT
                ).show()
            }) {

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"]="application/json"
                headers["Authapikey"] = token
                return headers
            }

            override fun getParams(): Map<String, String> {

                val params: MutableMap<String, String> = HashMap()

                params["order_id"] = order_id
                params["driver_id"] = driver_id
                params["order_status"] = st_status
                params["latitude"] = latitude
                params["longitude"] = longitude
                params["delivered_address"] = delivered_address

                return params
            }
        }
        // below line is to make
        // a json object request.
        // below line is to make
        // a json object request.
        queue.add(request)
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