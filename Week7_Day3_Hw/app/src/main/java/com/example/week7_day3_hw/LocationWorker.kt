package com.example.week7_day3_hw

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.*
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class LocationWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        showLastLocation()

        return Result.success()

    }

    @SuppressLint("MissingPermission")
    fun showLastLocation() {
        var locationManger = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager

        var location = locationManger.getLastKnownLocation(LocationManager.GPS_PROVIDER)


        val lat = location?.latitude
        val long = location?.longitude
        val time = Calendar.getInstance().timeInMillis


        addToFirebase(time, lat, long)


    }
         fun addToFirebase(time: Long, lat: Double?, long: Double?) {
            val geocoder = Geocoder(applicationContext)

             //it's application crash
                //val address = geocoder.getFromLocation(lat!!,long!!,2)

            val db = Firebase.firestore

            val locations = hashMapOf(
                "dateTime" to time,
                "latitude" to lat,
                "longitude" to long,
                //"address" to address[0].subAdminArea
            )

            db.collection("Location").add(locations)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "addNewLocation - addOnSuccessListener")
                }.addOnFailureListener {
                    Log.d(ContentValues.TAG, "addNewLocation - addOnFailureListener")
                }
        }


    }
