package com.waterdelivery.Serivice

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat


class LoactionUpdate(context: Context) : Service() {
    var longitude: String = ""
    var langitude: String = ""
    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }


    ////==========end permission=====
    fun getCurrentlocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
                Log.e("langitude", location.longitude.toString())
                Log.e("langitude", location.latitude.toString())

            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        try {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
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