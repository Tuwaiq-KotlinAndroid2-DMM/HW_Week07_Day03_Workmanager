package com.twqe.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore


class locationWorker(context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
) {
    override fun doWork(): Result {
        for (i in 1..30) {
            Thread.sleep((1000))
            showLastLocation()
        }
        return Result.success()
    }


    @SuppressLint("MissingPermission")
    fun showLastLocation() {

        var locationManager = applicationContext.getSystemService(LOCATION_SERVICE) as? LocationManager

        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 0, 0f,
            object : LocationListener {
                override fun onLocationChanged(location: Location) {


                    val latitude = location.latitude
                    val longitude = location.longitude
                    val time = Calendar.getInstance().timeInMillis
                    addToFirebase(time.toString(), latitude, longitude)

                }


            })
    }

    fun addToFirebase(time: String, latitude: Double?, longitude: Double?) {
        val db = FirebaseFirestore.getInstance()
        val locations = hashMapOf(
            "dateTime" to time,
            "latitude" to latitude,
            "longitude" to longitude,
        )
        db.collection("Location").add(locations)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "addNewLocation - addOnSuccessListener")
            }.addOnFailureListener {
                Log.d(ContentValues.TAG, "addNewLocation - addOnFailureListener")
            }
    }
}