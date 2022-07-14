package com.waterdelivery.customer.fragement

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.QrCodeActivity
import com.waterdelivery.R
import com.waterdelivery.customer.activity.CustomerActivity
import com.waterdelivery.customer.adpater.ListCustomerAdpater
import com.waterdelivery.customer.fragement.model.SearchResponse
import com.waterdelivery.main.DashBoardActivity
import com.waterdelivery.user.LoginActivity
import kotlinx.android.synthetic.main.fragment_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.util.*
import android.view.inputmethod.EditorInfo

import android.widget.TextView

import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import com.geelong.user.Util.Constants
import com.geelong.user.Util.FetchAddressServices
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


class ListCustomerFragment : Fragment() {
    var RECORD_REQUEST_CODE: Int = 401

    lateinit var sharprf: shareprefrences
    lateinit var dashBoardActivity: DashBoardActivity
    var input: String = ""

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
        return inflater.inflate(R.layout.fragment_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharprf = shareprefrences(requireActivity())
        dashBoardActivity = context as DashBoardActivity
        input = sharprf.getStringPreference("qrcode").toString()
        if (checkPermission() == true) {
            getCurrentlocation()

        } else {
            runtimePermission()
        }
        search.setText(sharprf.getStringPreference("qrcode").toString().toString())
        qrcode.setOnClickListener {
            startActivity(Intent(activity, QrCodeActivity::class.java))
        }

        search.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                input= v.text.toString()
                searchdta()
                return@OnEditorActionListener true
            }
            false
        })

//        search.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {}
//            override fun beforeTextChanged(
//                s: CharSequence, start: Int,
//                count: Int, after: Int
//            ) {
//            }
//
//            override fun onTextChanged(
//                s: CharSequence, start: Int,
//                before: Int, count: Int
//            ) {
//                if (s.length > 2) {
//                    input = search.text.toString()
//                    searchdta()
//                }
//            }
//        })


        img_add_topup.setOnClickListener {
            startActivity(Intent(context, CustomerActivity::class.java))
        }
       // resultReceiver = AddressResultReceiver(Handler())
/*
        if ((ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )

            //   Toast.makeText(requireContext(),"Permission",Toast.LENGTH_LONG).show()
        } else {
            dashBoardActivity.showProgressDialog()
            currentLocation
        }
*/
    }


    fun toastsucess(msg: String) {
        activity?.let {
            MotionToast.darkToast(
                it,
                "Hurray success 😍",
                msg,
                MotionToast.TOAST_SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(dashBoardActivity, R.font.helvetica_regular)
            )
        }
    }

    fun toasterror(msg: String) {

        MotionToast.darkToast(
            requireActivity(),
            "Sorry ☹ ",
            msg,
            MotionToast.TOAST_ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(dashBoardActivity, R.font.helvetica_regular)
        )

    }


    fun searchdta() {
        dashBoardActivity.showProgressDialog()
        val stringStringHashMap = HashMap<String, String>()
        stringStringHashMap.put("email", "")
        var signin: Call<SearchResponse> = APIUtils.getServiceAPI()!!.search(
            "Bearer " + sharprf.getStringPreference("token"),
            input
        )
        signin.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        dashBoardActivity.hideProgressDialog()

                        if (response.body()!!.status == true) {
                            sharprf.setStringPreference("qrcode", "")
                            recy_cutomer_list.layoutManager = LinearLayoutManager(context)
                            recy_cutomer_list.adapter =
                                context?.let { ListCustomerAdpater(it, response.body()!!.data) }
                            if (response.body()!!.data.size > 0) {
                                errormessage.visibility = View.GONE
                            } else {
                                errormessage.visibility = View.VISIBLE
                            }

                        } else {
                            toasterror(response.body()!!.message)
                        }

                    } else if (response.code() == 401) {
                        sharprf.clearAllPreferences()
                        startActivity(Intent(dashBoardActivity, LoginActivity::class.java))
                        dashBoardActivity.finishAffinity()


                    } else if (response.code() == 400) {
                        dashBoardActivity.hideProgressDialog()
                        dashBoardActivity.showToastMessage(
                            dashBoardActivity,
                            response.body()!!.message
                        )

                    } else if (response.code() == 500) {
                        dashBoardActivity.hideProgressDialog()
//                        mainacivity.showToastMessage(context,response!!.body()!!.message)

                    }

                } catch (e: Exception) {


                }

            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                dashBoardActivity.hideProgressDialog()
            }

        })
    }


    override fun onResume() {
        super.onResume()
        searchdta()
    }


    fun getCurrentlocation() {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
                try{
                    (context as DashBoardActivity).longitude = location.longitude.toString()
                    (context as DashBoardActivity).langitude = location.latitude.toString()
                    sharprf.setStringPreference("langitude",(context as DashBoardActivity).langitude!!)
                    sharprf.setStringPreference("longitude",(context as DashBoardActivity).longitude!!)
                    Log.e("langcccitude", location.longitude.toString())
                    Log.e("langcccitude", location.latitude.toString())

                }catch (e: Exception){

                }

            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        try {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireActivity(),
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


    fun runtimePermission() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), RECORD_REQUEST_CODE
            )
        }
    }

    //////======if permission granted=====
    fun checkPermission(): Boolean? {
        val FirstPermissionResult =
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
        val SecondPermissionResult =
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val thirdPermissionResult =
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
        val FourPermissionResult =
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
        val FivePermissionResult =
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED && SecondPermissionResult == PackageManager.PERMISSION_GRANTED && thirdPermissionResult == PackageManager.PERMISSION_GRANTED && FourPermissionResult == PackageManager.PERMISSION_GRANTED && FirstPermissionResult == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    val camerapermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val WriteStoragePermission =
                        grantResults[1] == PackageManager.PERMISSION_GRANTED
                    val ReadStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED
                    val FineLocationPermission =
                        grantResults[3] == PackageManager.PERMISSION_GRANTED
                    val CoarsePermission = grantResults[4] == PackageManager.PERMISSION_GRANTED
                    if (camerapermission && WriteStoragePermission && ReadStoragePermission && FineLocationPermission && CoarsePermission) {
                        Toast.makeText(requireActivity(), "Permission Granted", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_LONG).show()
                    }
                } else {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                }
                if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.size > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        dashBoardActivity.showProgressDialog()
                        currentLocation
                    } else {
                        // Toast.makeText(requireContext(), "Permission is denied!", Toast.LENGTH_SHORT).show()
                    }
                }

                return
            }
        }
    }


    private val currentLocation: Unit
        private get() {

            val locationRequest = LocationRequest()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 3000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            LocationServices.getFusedLocationProviderClient(requireContext())
                .requestLocationUpdates(locationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        LocationServices.getFusedLocationProviderClient(requireContext())
                            .removeLocationUpdates(this)
                        if (locationResult.locations != null) {
                            if (locationResult.locations.size > 0) {
                                val latestlocIndex = locationResult.locations.size - 1
                                val lati = locationResult.locations[latestlocIndex].latitude
                                val longi = locationResult.locations[latestlocIndex].longitude
                                lati_curr = lati.toString()
                                longi_current = longi.toString()
                                /*SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                                    ConstantUtils.LATITUDE, lati_curr)
                                SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                                    ConstantUtils.LONGITUDE, longi_current)*/

                                val location = Location("providerNA")
                                location.longitude = longi
                                location.latitude = lati
                                try {
                                    sharprf.setStringPreference("langitude",lati_curr)
                                    sharprf.setStringPreference("longitude",longi_current)
                                }catch (e:Exception){
                                    Toast.makeText(requireContext(),e.toString(),Toast.LENGTH_SHORT).show()

                                }
                                dashBoardActivity.hideProgressDialog()
                                //searchdta()
                               // Toast.makeText(requireContext(),lati_curr+" "+longi_current,Toast.LENGTH_SHORT).show()
                               // fetchaddressfromlocation(location)
                            } else {

                            }
                        }
                    }
                }, Looper.getMainLooper())
        }


/*
    private fun fetchaddressfromlocation(location: Location) {
        val intent = Intent(requireContext(), FetchAddressServices::class.java)
        intent.putExtra(Constants.RECEVIER, resultReceiver)
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location)
        requireContext().startService(intent)
    }
*/

}