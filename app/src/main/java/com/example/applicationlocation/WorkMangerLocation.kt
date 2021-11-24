package com.example.applicationlocation

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

class WorkMangerLocation  (context: Context, workerParameters: WorkerParameters)
    : Worker(context,workerParameters) {
    override fun doWork(): Result {
        getLastLocation()
        return Result.success()
    }


    @SuppressLint("MissingPermission")
    fun getLastLocation(){
        Log.d(ContentValues.TAG, "getLastLocation: starting")
        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        val time = Calendar.getInstance().timeInMillis
        val  (lat, long) = Pair(location?.latitude, location?.longitude)


        val intent = Intent("location")
        intent.putExtra("date", time)
        intent.putExtra("lat", lat)
        intent.putExtra("long", long)
        applicationContext.sendBroadcast(intent)
    }
    fun Firebase(time: Long, lat: Double?, long: Double?) {
    val geocoder = Geocoder(applicationContext)
    val address = geocoder.getFromLocation(lat!!,long!!,2)

    val db = Firebase.firestore

    val locations = hashMapOf(
        "dateTime" to time,
        "latitude" to lat,
        "longitude" to long,
        "address" to address[0].subAdminArea
    )

    db.collection("Location").add(locations)
        .addOnSuccessListener {
            Log.d(ContentValues.TAG, "NewLocation - OnSuccessListener")
        }.addOnFailureListener {
            Log.d(ContentValues.TAG, "NewLocation - OnFailureListener")
        }
}
}





