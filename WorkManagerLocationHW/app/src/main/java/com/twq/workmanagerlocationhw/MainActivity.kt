package com.twq.workmanagerlocationhw

import android.Manifest
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.twq.workmanagerlocationhw.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // onStart, app collects location
        checkPermission()

        setContentView(binding.root)
    }


    fun checkPermission(){
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
            val locationWorkerRequest = PeriodicWorkRequestBuilder<LocationWorker>(10, TimeUnit.SECONDS).build()

            WorkManager.getInstance(this).enqueue(locationWorkerRequest)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            checkPermission()
        } else {
            AlertDialog.Builder(this).apply {
                title = "Warning!"
                setMessage(" To use the application, you must enable location from the settings")
                setPositiveButton("okay", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val intentSetting = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri: Uri = Uri.fromParts("package", packageName, null)
                        intentSetting.data = uri
                        startActivity(intentSetting)
                    }
                })
            }.show()
        }
    }
}