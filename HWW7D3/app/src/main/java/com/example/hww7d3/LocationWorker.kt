package com.example.hww7d3

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class LocationWorker(context: Context, workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        getlocation()
        return Result.success()
    }
        @SuppressLint("MissingPermission")
        fun getlocation() {
            var locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            var time = Date()
            var lat = location?.latitude
            var long = location?.longitude

            var geocoder = Geocoder(applicationContext)

            val db = Firebase.firestore

            val locations = hashMapOf("dateTime" to time, "latitude" to lat, "longitude" to long)

            db.collection("Location").add(locations)
                .addOnSuccessListener {
                    println("succeeded")
                }.addOnFailureListener {
                    println("failed ${it.message}")
                }
        }


        }

