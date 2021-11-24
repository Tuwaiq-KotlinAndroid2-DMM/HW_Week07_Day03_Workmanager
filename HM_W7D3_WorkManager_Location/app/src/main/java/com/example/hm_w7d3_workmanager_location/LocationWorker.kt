package com.example.hm_w7d3_workmanager_location

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LocationWorker(context: Context, workerParameters: WorkerParameters): Worker(context,workerParameters) {

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {

        var locationManger = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        var place= locationManger.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        var Database=Firebase.firestore
        var location= hashMapOf("long" to place?.longitude, "Lat" to place?.latitude, "time" to place?.accuracy)

        Log.d("Firebase", "Location")

        Database.collection("location").add(location)
            .addOnSuccessListener {
                print("location added successfully")
            }
            .addOnFailureListener {
                print("location was not added")
            }

       return Result.success()
    }







       /* var Longitude= place?.longitude
        var Lattitude= place?.latitude
        var time= place?.time
        SendToDB(Longitude,Lattitude,time)*/

    //private fun saveLocation(location: Location) =
    //      GlobalScope.launch { database.locationDao().insert(location) }
    // val latitude = loc.latitude

  /*  fun SendToDB(LONGITUDE: Double?, LATITUDE: Double?, TimeMillis: Long?){


    }*/

    //java
    //.addOnSuccessListener {
    //                    enableLocation.value = Response.success(true)
    //            }
    //            .addOnFailureListener {
    //                Timber.e(it, "Gps not enabled")
    //                enableLocation.value = Response.error(it)

    // getCompleteAddressString(double LATITUDE, double LONGITUDE)

    /*    if (location != null) {
             saveLocation(
                 Location(0,
                     location.latitude,
                     location.longitude,
                     System.currentTimeMillis()
                 )
             )
         }*/
}