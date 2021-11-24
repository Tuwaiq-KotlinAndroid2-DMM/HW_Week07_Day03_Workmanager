package com.example.workmanagerhw

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class LocationWorker(context: Context, workerParameters: WorkerParameters):Worker(context,workerParameters) {

    override fun doWork(): Result {
        showLocation()

        return Result.success()
    }

    @SuppressLint("MissingPermission")
    fun showLocation(){
        val lm = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        Handler(Looper.getMainLooper()).post {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f){
                val location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                val latitude = location?.latitude
                val longitude = location?.longitude
                val time = Calendar.getInstance().timeInMillis
                println(location)
                println(time)

                FireBase(time,latitude,longitude)
            }
        }
        
        }


        fun FireBase(time: Long, latitude: Double?, longitude: Double?){
            val geocoder = Geocoder(applicationContext)
        val address = geocoder.getFromLocation(latitude!!,longitude!!,2)
            val db = Firebase.firestore
            val location = hashMapOf(
                "date" to time,
            "latitude" to latitude,
            "longitude" to longitude,
                "address" to address[0].subAdminArea
            )

            db.collection("AddLocationToFirebase").add(location)
                .addOnSuccessListener{
                    println("Add location")
                }
                .addOnFailureListener{
                    println("Failed to Add location")

                }

        }


}