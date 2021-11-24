package com.example.hw_week7application


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class LocationWorker(context:Context, workerParameters: WorkerParameters):Worker(context,workerParameters) {
    override fun doWork(): Result {
        getLocation()
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        var locationManager = applicationContext.getSystemService(LOCATION_SERVICE) as? LocationManager
        var location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        var lat = location?.latitude.toString()
        var lon = location?.longitude.toString()
        var time = Calendar.getInstance().timeInMillis.toString()

        addToFirebase(lat, lon, time)

    }

    fun addToFirebase(lat:String, lon:String, time:String){
        val db = Firebase.firestore

        var location = hashMapOf(
            "time" to time,
            "lat" to lat,
            "lon" to lon
        )
        db.collection("Locations").add(location)
            .addOnCompleteListener {
                println("New location added")
            }
    }
}