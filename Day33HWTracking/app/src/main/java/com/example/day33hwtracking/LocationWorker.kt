package com.example.day33hwtracking

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

class LocationWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        getLastKnownLocation()
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(){
        Log.d(ContentValues.TAG, "getLastKnownLocation: starting")
        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        val time = Calendar.getInstance().timeInMillis
        val  (lat, long) = Pair(location?.latitude, location?.longitude)

        sendToFirebase(time, lat, long)

        val intent = Intent("update_location")
        intent.putExtra("date", time)
        intent.putExtra("lat", lat)
        intent.putExtra("long", long)
        applicationContext.sendBroadcast(intent)
    }

    fun sendToFirebase(time: Long, lat: Double?, long: Double?) {
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
                Log.d(ContentValues.TAG, "addNewLocation - addOnSuccessListener")
            }.addOnFailureListener {
                Log.d(ContentValues.TAG, "addNewLocation - addOnFailureListener")
            }
    }
}