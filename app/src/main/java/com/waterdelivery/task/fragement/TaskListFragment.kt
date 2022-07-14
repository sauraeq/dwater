package com.waterdelivery.task.fragement

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.geelong.user.Util.Constants
import com.geelong.user.Util.FetchAddressServices
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.waterdelivery.R
import com.waterdelivery.main.DashBoardActivity
import com.waterdelivery.task.adpater.DateAdapter
import com.waterdelivery.task.modal.TaskListResponse
import com.waterdelivery.user.LoginActivity
import kotlinx.android.synthetic.main.fragment_task_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class TaskListFragment : Fragment() {
    lateinit var sharprf: shareprefrences
    lateinit var mainactivity: DashBoardActivity
    lateinit var latitude:String
    lateinit var longitude:String
    var location:String=""
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    var lati_curr:String = ""
    var longi_current:String =""
    var locat:String = ""
    var resultReceiver: ResultReceiver? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        sharprf = shareprefrences(requireContext())
        mainactivity = context as DashBoardActivity

        var args=this.arguments
        val date=args?.getString("date")
       // Toast.makeText(requireContext(),date,Toast.LENGTH_SHORT).show()

        try {
            latitude= sharprf.getStringPreference("langitude").toString()
            longitude= sharprf.getStringPreference("longitude").toString()
            getCompleteAddressString(latitude.toDouble(),longitude.toDouble())
        }catch (e:Exception){

        }

       orderlist(date.toString())
    }
    private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String? {
        var strAdd = ""
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.getMaxAddressLineIndex()) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
               // Log.w("My Current loction address", strReturnedAddress.toString())
            } else {
               // Log.w("My Current loction address", "No Address returned!")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
           // Log.w("My Current loction address", "Canont get Address!")
        }
        location=strAdd
        return strAdd
    }

    fun orderlist(date:String) {
        mainactivity.showProgressDialog()
        var signin: Call<TaskListResponse> = APIUtils.getServiceAPI()!!.tasklist(
            "Bearer " + sharprf.getStringPreference("token"),
            sharprf.getStringPreference("customer_id").toString(),date
        )
        signin.enqueue(object : Callback<TaskListResponse> {
            override fun onResponse(
                call: Call<TaskListResponse>,
                response: Response<TaskListResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        mainactivity.hideProgressDialog()
                        if (response.body()!!.status == true) {
                            recy_task_list.layoutManager = LinearLayoutManager(context)
                            Log.e("datatattat", response.body()!!.data.toString())
                            recy_task_list.adapter =
                                DateAdapter(
                                    activity!!,latitude,longitude,location,
                                    response.body()!!.data as ArrayList<TaskListResponse.Data>
                                )
                            if (response.body()!!.data.size > 0) {
                                errortask.visibility = View.GONE
                            } else {
                                errortask.visibility = View.VISIBLE
                            }
                        } else {

                        }

                    } else if (response.code() == 401) {
                        sharprf.clearAllPreferences()
                        startActivity(Intent(activity, LoginActivity::class.java))
                        activity!!.finishAffinity()

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<TaskListResponse>, t: Throwable) {
                mainactivity.hideProgressDialog()
                Log.e("hello", t.message.toString())
            }

        })
    }





}