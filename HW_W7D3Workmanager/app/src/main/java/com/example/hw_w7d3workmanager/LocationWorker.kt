package com.example.hw_w7d3workmanager

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class LocationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {

        //    private lateinit var fusedLocationClient: FusedLocationProviderClient

        showLocation()
        return Result.success()

    }


    @SuppressLint("MissingPermission")
    fun showLocation(){


     /*   fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
            }*/
        val locationManger = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager

        var location = locationManger.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        val time = Calendar.getInstance().timeInMillis
        val lat = location?.latitude
        val long = location?.longitude


        addToFB(time, lat, long)


    }
    // add location to firebase
    fun addToFB(time:Long,latitude:Double?,longitude:Double?){

        val geocoder = Geocoder(applicationContext)
        var address = geocoder.getFromLocation(latitude!!, longitude!!, 2)
        var locationAddress = address[0]
        val db = Firebase.firestore

        val locations = hashMapOf(
            "time" to time,
            "latitude" to latitude,
            "longitude" to longitude,
            "address" to locationAddress.countryName+", "+locationAddress.subAdminArea

        )


        db.collection("Location").add(locations)
            .addOnSuccessListener {
                println("Add location successfuly")            }
            .addOnFailureListener {
                println("Failed to Add location")
            }



    }



}