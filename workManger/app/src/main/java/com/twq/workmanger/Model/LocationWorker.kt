package com.twq.workmanger.Model

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.twq.workmanger.MainActivity
import java.util.*




class LocationWorker(context: Context, workerParameters: WorkerParameters): Worker(context,workerParameters) {
    override fun doWork(): Result {


        getLocation()
//        val output: Data = workDataOf("name"  to "mashael")
//        return Result.success(output)
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    fun getLocation(){
        var locationManager=applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        var longitude=location?.longitude
        var latitude=location?.latitude


        var geocoder = Geocoder(applicationContext)
        var address = geocoder.getFromLocation(latitude!!, longitude!!, 2)
        var locationAddress = address[0]

        val db = Firebase.firestore

        val locations = hashMapOf(
            "latitude" to latitude,
            "longitude" to longitude,
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






//        for (i in 1..30) {
//            println(Date())
//            Thread.sleep(1000)
//        }
