package com.waterdelivery.task.fragement

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.waterdelivery.R
import com.waterdelivery.main.DashBoardActivity
import com.waterdelivery.task.modal.MapModal
import com.waterdelivery.task.modal.TaskListResponse
import com.waterdelivery.user.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapFragment : Fragment(), OnMapReadyCallback {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var map: GoogleMap? = null
    lateinit var sharprf: shareprefrences
    lateinit var mainactivity: DashBoardActivity
    lateinit var maplist: ArrayList<TaskListResponse.Data>
    var markerList = ArrayList<MapModal>()
    private var mOrigin: LatLng? = null
    private var mDestination: LatLng? = null
    private val mPolyline: Polyline? = null
    private var mMarkerOptions: MarkerOptions? = null
    var supportMapFragment: SupportMapFragment?=null
    private lateinit var latLngList:ArrayList<LatLng>
    private var locationList: ArrayList<String> =
        ArrayList()
    private var delivered_status: ArrayList<String> =
        ArrayList()
    private var mLocationManager: LocationManager? = null
    private var mLocationListener: LocationListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharprf = shareprefrences(requireContext())
        mainactivity = context as DashBoardActivity
        maplist = arrayListOf()
        markerList = arrayListOf()
        var args=this.arguments
        val date=args?.getString("date")
        orderlist(date.toString())
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        supportMapFragment =
            childFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment?
        supportMapFragment!!.getMapAsync(this)

        /*   val supportMapFragment =
               childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
           supportMapFragment!!.getMapAsync { googleMap ->
               googleMap.setOnMapClickListener { latLng ->
                   val markerOptions = MarkerOptions()
                   markerOptions.position(latLng)
                   markerOptions.title(latLng.latitude.toString() + ":" + latLng.longitude)
                   googleMap.clear()
                   googleMap.animateCamera(
                       CameraUpdateFactory.newLatLngZoom(
                           latLng, 0f
                       )
                   )
                   googleMap.addMarker(markerOptions)
               }
           }*/
        //getMyLocation()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getMyLocation() {

        // Getting LocationManager object from System Service LOCATION_SERVICE
        mLocationManager = requireActivity()!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                mOrigin = LatLng(location.latitude, location.longitude)
                mDestination = LatLng(28.6060242, 77.4274728)
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin, 12f))

            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        val currentApiVersion = Build.VERSION.SDK_INT
        if (currentApiVersion >= Build.VERSION_CODES.M) {
            if (requireActivity()!!.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                map?.setMyLocationEnabled(true)
                mLocationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000,
                    0f,
                    mLocationListener as LocationListener
                )
                map?.setOnMapLongClickListener(OnMapLongClickListener { latLng ->
                    mDestination = latLng
                    map?.clear()
                    mMarkerOptions = MarkerOptions().position(mDestination).title("Destination")
                    map?.addMarker(mMarkerOptions)

                })
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), 100
                )
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        Log.e("working", "working")
        map = googleMap

//        val latLng = LatLng(28.4744, 77.5040)
//
//        val markerOptions = MarkerOptions().position(latLng).title("I am here!")
//        map?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
//        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
//        map?.addMarker(markerOptions)
    }
    private  var builder: LatLngBounds.Builder? = null
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
                            maplist = response.body()!!.data as ArrayList<TaskListResponse.Data>
                            //  setUpMapIfNeeded()

                            latLngList =ArrayList()
                            for (i in 0 until maplist.size) {
                                for (j in 0 until maplist[i].Order_List.size) {


                                    val markerOptions = MarkerOptions()
                                    val loc=maplist[i]
                                    val zoomLevel = 15.0
                                    var locationName=""
                                    var latitudes:Double=0.0
                                    var longitudes:Double=0.0
                                    try {

                                        latitudes = maplist[i].Order_List[j].latitude.toDouble()
                                        longitudes = maplist[i].Order_List[j].longitude.toDouble()

                                        locationName = maplist[i].Order_List[j].address.city

                                    }catch (e:Exception){
                                        locationName=""
                                    }
                                    latLngList.add( LatLng(latitudes, longitudes))
                                    locationList.add(locationName)
                                    delivered_status.add(maplist[i].Order_List[j].delever_status)
                                    //Log.d("TAGG", ": ${loc.latitude!!.toDouble()} -- ${loc.longitude!!.toDouble()} ")
                                   /* val pos = LatLng(latitudes, longitudes)
                                    val bitmap =createStoreMarker("${i + 1}")
                                    markerOptions.position(pos)
                                        .anchor(0.5f, 0.5f)
                                        .title(locationName)
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                    map?.addMarker(markerOptions)
                                    builder?.include(markerOptions.position);
                                    map?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, zoomLevel.toFloat()))
*/


/*




                                    var latitude: Double = maplist[i].Order_List[j].address.latitude.toDouble()
                                    var longitude = maplist[i].Order_List[j].address.longitude.toDouble()
                                    var title = maplist[i].Order_List[j].address.city
                                    markerList.add(MapModal(latitude, longitude, title))

                                    for (point in markerList) {
                                        val markerOptions = MarkerOptions().position(
                                            LatLng(
                                                point.latitutde,
                                                point.longitude
                                            )
                                        ).title(point.title)
                                        map?.animateCamera(
                                            CameraUpdateFactory.newLatLng(
                                                LatLng(
                                                    point.latitutde,
                                                    point.longitude
                                                )
                                            )
                                        )
                                        map?.animateCamera(
                                            CameraUpdateFactory.newLatLngZoom(
                                                LatLng(
                                                    point.latitutde,
                                                    point.longitude
                                                ), 15f
                                            )
                                        )
                                        map?.addMarker(markerOptions)


                                    }
*/

                                }

                                add_map()
                               // return

                            }



                            /* val line: Polyline = googleMap.addPolyline(PolylineOptions().width(3f).color(Color.BLUE))
                             line.points = latLngList*/


                        } else {

                        }

                    } else if (response.code() == 401) {

                        sharprf.clearAllPreferences()
                        startActivity(Intent(activity, LoginActivity::class.java))
                        activity!!.finishAffinity()

                    }

                } catch (e: Exception) {
                    //add_map()
                    Log.e("hello", "unaunried")
                }

            }

            override fun onFailure(call: Call<TaskListResponse>, t: Throwable) {
                mainactivity.hideProgressDialog()
                Log.e("hello", "unaunried")
            }

        })
    }

    private fun add_map(){
        for (i in 0 until  latLngList.size ){

            if (delivered_status[i].equals("Delivered")){

            }
            else{
                val markerOptions = MarkerOptions()
                val loc=latLngList[i]
                val zoomLevel = 15.0
                Log.d("TAGG", ": ${loc.latitude!!.toDouble()} -- ${loc.longitude!!.toDouble()} ")
                val pos = LatLng(loc.latitude!!.toDouble(), loc.longitude!!.toDouble())
                val bitmap =createStoreMarker("${i + 1}")
                markerOptions.position(pos)
                    .anchor(0.5f, 0.5f)
                    .title(locationList[i])
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                map?.addMarker(markerOptions)
                builder?.include(markerOptions.position);
                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, zoomLevel.toFloat()))
            }


        }


        val dashedPattern: List<PatternItem> = listOf(Dash(10F), Gap(10F))
        val dashedPattern1: List<AbstractSafeParcelable> = listOf(RoundCap())
        map?.addPolyline(
            PolylineOptions()
                .addAll(latLngList)
                .pattern(dashedPattern)
                .color(Color.parseColor("#00a3fe"))
        )
    }
    private fun createStoreMarker(count:String): Bitmap? {
        val markerLayout: View = layoutInflater.inflate(R.layout.marker_layout, null)
        val markerImage = markerLayout.findViewById<View>(R.id.marker_image) as ImageView
        val markerRating = markerLayout.findViewById<View>(R.id.marker_text) as TextView
        markerImage.setImageResource(R.drawable.map_marker)
        markerRating.text = count
        markerLayout.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        markerLayout.layout(0, 0, markerLayout.measuredWidth, markerLayout.measuredHeight)
        val bitmap = Bitmap.createBitmap(
            markerLayout.measuredWidth,
            markerLayout.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        markerLayout.draw(canvas)
        return bitmap
    }

}


