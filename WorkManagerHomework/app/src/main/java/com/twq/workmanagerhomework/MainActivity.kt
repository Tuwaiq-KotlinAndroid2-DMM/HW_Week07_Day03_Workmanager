package com.twq.workmanagerhomework

import android.Manifest
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.twq.workmanagerhomework.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit
import java.util.*
class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView
    lateinit var textView2: TextView
    lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = FirebaseFirestore.getInstance()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val worker = PeriodicWorkRequestBuilder<LocationWorker>(10, TimeUnit.SECONDS)
        var wm = WorkManager.getInstance()
        binding.button.setOnClickListener {
           wm.enqueue(worker.build())
           check()
        }

        setContentView(binding.root)
    }

    fun check(){
        var locationManager = getSystemService(LOCATION_SERVICE) as? LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),0
            )
        }

        else{
            showLocation()


        }
    }

    fun showLocation() {
        textView = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView2)

        var locationManager = getSystemService(LOCATION_SERVICE) as? LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,0,0f,
            object: LocationListener {
                override fun onLocationChanged(location: Location) {
                    //location.bearing

                    val l = hashMapOf("latitude" to location.latitude,
                                    "longitude" to location.longitude
                    )
                    db.collection("Location")
                        .add(l)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                    //textView.text = "${location.latitude}   ${location.longitude}"
                    //this@MainActivity or context
                    Thread(){
                        var geocoder = Geocoder(this@MainActivity)
                        var l = geocoder.getFromLocation(location.latitude,location.altitude,10)
                        //l[0].getAddressLine()
                        val address = l[0]
                        println(address.countryName + " "+address.adminArea)
                        println(address.getAddressLine(0)+" "+address.getAddressLine(1))

                        runOnUiThread {
                            textView2.text = address.getAddressLine(0)+" "+address.getAddressLine(1)
                        }
                    }.start()
                }
            })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            showLocation()
        }else{
            AlertDialog.Builder(this).apply {
                title = "Warning"
                setMessage("To access location allow the permission")
                setPositiveButton("Ok",object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri : Uri = Uri.fromParts("package",packageName,null)
                        intent.data = uri
                        startActivity(intent)
                    }
                })
            }
        }
    }
}

