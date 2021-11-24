package com.example.hw_week7_day3

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class LocationWorker(context: Context, workerParams :WorkerParameters): Worker (context,workerParams) {
    override fun doWork(): Result {

        showLocation()
        return Result.success()

    }


    @SuppressLint("MissingPermission")
    fun showLocation(){

        val locationManger = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager

        var location = locationManger.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        val time = Calendar.getInstance().timeInMillis
        val lat = location?.latitude
        val long = location?.longitude


        addToFB(time, lat, long)
    }
    // add location to firebase
    fun addToFB(time:Long,latitude:Double?,longitude:Double?){


        val db = Firebase.firestore

        val location = hashMapOf(
            "time" to time,
            "latitude" to latitude,
            "longitude" to longitude,

            )

        db.collection("Location").add(location)
            .addOnSuccessListener {
                println("Add location successfuly")            }
            .addOnFailureListener {
                println("Failed to Add location")
            }
    }
}