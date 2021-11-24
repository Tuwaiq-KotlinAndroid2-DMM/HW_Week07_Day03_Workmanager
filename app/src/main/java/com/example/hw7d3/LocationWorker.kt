package com.example.hw7d3

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
val db = Firebase.firestore
const val WORKER_RESULT_INT = "WORKER_RESULT_INT"

class LocationWorker(appContext: Context, workerParams: WorkerParameters):
Worker(appContext, workerParams){
    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        var n=inputData.getInt("Loop",1)
       // for (i in 1..30){
         //   println(Date())
         //   Thread.sleep(1000)

      //  }

        var locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        val db = Firebase.firestore

        // Create a new user with a first and last name
        val userLocation = hashMapOf(
            "lat" to location!!.latitude,
            "long" to location.longitude
        )

        db.collection("locations")
            .add(userLocation)
            .addOnSuccessListener { documentReference ->
                Log.d("LocationWorker", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("LocationWorker", "Error adding document", e)
            }


        return Result.success()
    }

}
