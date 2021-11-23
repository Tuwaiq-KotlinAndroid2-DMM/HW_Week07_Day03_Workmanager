package com.example.day33hwtracking

import android.Manifest
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.day33hwtracking.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    val broadCastReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val date = intent?.getLongExtra("date", 0)
            val lat = intent?.getDoubleExtra("lat", 0.0)
            val long = intent?.getDoubleExtra("long", 0.0)

            val geocoder = Geocoder(this@MainActivity)
            val address = geocoder.getFromLocation(lat!!,long!!,2)

            //binding.textViewAddress.text = address[0].countryName + " " + address[0].locale
            //binding.textViewLocation.text = "$lat --- $long"
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        getLastKnownLocation()

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(broadCastReceiver, IntentFilter("update_location"))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadCastReceiver)
    }

    fun getLastKnownLocation(){
        if ( ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                7
            )
        } else {
            setWorkManager()
        }
    }

    fun setWorkManager(){
        val locationWorkerRequest = PeriodicWorkRequestBuilder<LocationWorker>(10, TimeUnit.SECONDS).build()

        WorkManager.getInstance(this).enqueue(locationWorkerRequest)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getLastKnownLocation()
        } else {
            AlertDialog.Builder(this).apply {
                title = "Permission required"
                setMessage(" To access location, go to settings -> Allow location service")
                setPositiveButton("okay", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        if (dialog != null) {
                            dialog.dismiss()
                        }
                    }
                })
            }.show()
        }
    }
}