package com.example.workmanagerhw
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Data
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.withTimeout
import java.util.*

class LocationWorker(context: Context, workerParameters: WorkerParameters):Worker(context,workerParameters) {

    override fun doWork(): Result {
        showLocation()

        return Result.success()
    }

    @SuppressLint("MissingPermission")
    fun showLocation(){
        val lm = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        val latitude = location?.latitude
        val longitude = location?.longitude
        val time = Calendar.getInstance().timeInMillis

        FireBase(time,latitude,longitude)



        }

    /*fun showLocation() {

        var locationManager = applicationContext.getSystemService(LOCATION_SERVICE) as? LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,
            object: LocationListener {
                override fun onLocationChanged(location: Location) {
                   // textview.text = "${location.latitude}   ${location.longitude}"

                    Thread(){
                        var geocoder = Geocoder(this@LocationWorker)
                        var listOfAddress = geocoder.getFromLocation(location.latitude,location.longitude,10)

                        val address = listOfAddress[0]
                        println(address.countryName+" "+address.adminArea)
                        println(address.getAddressLine(0)+" "+address.getAddressLine(1)+" "+address.getAddressLine(2))

                        runOnUiThread{
                            textviewAddress.text = address.getAddressLine(0)+" "+address.getAddressLine(1)+" "+address.getAddressLine(2)
                        }
                    }.start()

                }
            })

    }*/
        fun FireBase(time: Long, latitude: Double?, longitude: Double?){
            val geocoder = Geocoder(applicationContext)
            val db = Firebase.firestore
            val location = hashMapOf(
                "date" to time,
            "latitude" to latitude,
            "longitude" to longitude
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