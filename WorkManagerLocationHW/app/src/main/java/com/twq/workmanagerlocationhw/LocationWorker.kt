package com.twq.workmanagerlocationhw

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.LocationManager
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class LocationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        getCurrentLocation()
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        var locationManager =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        // currnt time,lat,long, address
        var time = Date()
        var lat = location?.latitude
        var long = location?.longitude

        var geocoder = Geocoder(applicationContext)
        var address = geocoder.getFromLocation(lat!!, long!!, 2)
        var locationAddress = address[0]

        // send these info to db
        val db = Firebase.firestore

        val locations = hashMapOf(
            "dateTime" to time,
            "latitude" to lat,
            "longitude" to long,
            "address" to locationAddress.countryName+", "+locationAddress.subAdminArea
        )

        db.collection("Location").add(locations)
            .addOnSuccessListener {
                println("succeeded")
            }.addOnFailureListener {
                println("failed because: ${it.message}")
            }
    }

}