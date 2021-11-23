package com.example.hm_w7d3_workmanager_location

import android.Manifest
import android.annotation.SuppressLint
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
import android.renderscript.ScriptGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.hm_w7d3_workmanager_location.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var textview: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var binding = ActivityMainBinding.inflate(layoutInflater)
        val workManager = WorkManager.getInstance(applicationContext)

        binding.button.setOnClickListener {

           checkPermissionForLocation()


        }

        setContentView(binding.root)


    }
    fun checkPermissionForLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {

            // show request permission dialog
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),1)

        }else{
            var worker = PeriodicWorkRequestBuilder<LocationWorker>(10, TimeUnit.SECONDS)

            var wm = WorkManager.getInstance(this)

        }

    }





    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

            checkPermissionForLocation()
        }else{

            AlertDialog.Builder(this).apply {
                title= "warning"
                setMessage("To access location go to Setting-> allow location service")
                setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {


                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri: Uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }

                })
            }.show()
        }


    }
}

