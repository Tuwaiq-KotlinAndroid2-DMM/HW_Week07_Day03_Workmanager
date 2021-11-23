package com.example.week7day3workmanger

import android.app.Activity
import android.content.ContentValues
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        binding.button.setOnClickListener{
            var worker= OneTimeWorkRequestBuilder<LocationWorker>()



            var data=Data.Builder()
                .putInt("loop",25)
            
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



        val bindingsActivityMainActivity.infl


        val worker :WorkRequest = OneTimeWorkRequestBuilder<LocationWorker>().build()
        binding.button.setOnClickListener {
            WorkManager.getInstance(this).enqueue(worker)

        }



        setContentView(binding.root)
    }
}


    }
}