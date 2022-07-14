package com.waterdelivery.Serivice

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.ActivityManager.RunningTaskInfo
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.waterdelivery.orderdelivery.OrderDiliveryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationService : Service() {
    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if (locationResult != null && locationResult.lastLocation != null) {
                val lat = locationResult.lastLocation.latitude
                val lon = locationResult.lastLocation.longitude
                val bearing = locationResult.lastLocation.bearing.toDouble()
                Log.d(
                    "====VENDOR_LOCATION",
                    lat.toString() + " , " + lon + " : " + locationResult.lastLocation.bearing
                )
                updateVendorLocations(lat, lon, bearing)
                sendData(lat, lon, locationResult.lastLocation.bearing)
            }
        }
    }

    private fun updateVendorLocations(lat: Double, lon: Double, bearing: Double) {
        val prefManager = shareprefrences(applicationContext)
        APIUtils.getServiceAPI()!!.driverLocationUpdate(
            "Bearer " + prefManager.getStringPreference("token").toString(),
            prefManager.getStringPreference("customer_id").toString(),
            lat.toString(),
            lon.toString()
        )
            .enqueue(object : Callback<OrderDiliveryResponse> {
                override fun onResponse(
                    call: Call<OrderDiliveryResponse>,
                    response: Response<OrderDiliveryResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status) {
                        } else {

                        }
                    } else {
                        Toast.makeText(this@LocationService, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<OrderDiliveryResponse>, t: Throwable) {
                    Toast.makeText(this@LocationService, "Error :-" + t.message, Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    @SuppressLint("MissingPermission")
    private fun startLocationService() {
//        String channel_id = "location_channel";
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent result = new Intent();
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                getApplicationContext(), 0, result, PendingIntent.FLAG_UPDATE_CURRENT
//        );
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id);
//
//        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
//        builder.setContentTitle("Location Service");
//        builder.setDefaults(Notification.DEFAULT_ALL);
//        builder.setContentText("Running");
//        builder.setContentIntent(pendingIntent);
//        builder.setAutoCancel(true);
//        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        val request = LocationRequest()
        request.interval = 300000
        request.fastestInterval = 300000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
        //        startForeground(Constants.LOCATION_SERVICE_ID,builder.build());
    }

    private fun stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this)
            .removeLocationUpdates(locationCallback)
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationService()
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet Implemented")
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        onTaskRemoved(intent)
        if (intent != null) {
            val action = intent.action
            if (action != null) {
                if (action == "startlocationservice") {
                    startLocationService()
                } else if (action == "stoplocationservice") {
                    stopLocationService()
                }
            }
        }
        return START_STICKY
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onTaskRemoved(rootIntent: Intent) {
//        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
//        restartServiceIntent.setPackage(getPackageName());
////            startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent)
    }

    // App is killed
    // App is in background or foreground
    private val isAppRunning: Boolean
        private get() {
            val m = this.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            val runningTaskInfoList = m.getRunningTasks(10)
            val itr: Iterator<RunningTaskInfo> = runningTaskInfoList.iterator()
            var n = 0
            while (itr.hasNext()) {
                n++
                itr.next()
            }
            return n != 1
            // App is in background or foreground
        }

    private fun sendData(lat: Double, lon: Double, bearing: Float) {
        val intent = Intent(KEY)
        intent.putExtra(LATITUDE, lat)
        intent.putExtra(LONGITUDE, lon)
        intent.putExtra(BEARING, bearing)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    companion object {
        const val KEY = "vendor"
        const val LATITUDE = "vendorlatitude"
        const val LONGITUDE = "vendorlongitude"
        const val BEARING = "vendorbearing"
    }
}