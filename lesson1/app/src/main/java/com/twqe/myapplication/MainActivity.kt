package com.twqe.myapplication

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.work.*
import com.twqe.myapplication.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.button.setOnClickListener {
            var worker = PeriodicWorkRequestBuilder<locationWorker>(10, TimeUnit.SECONDS)
            var wm = WorkManager.getInstance(this)
            wm.enqueue(worker.build())
            checkedPermision()
        }

        //        location

        setContentView(binding.root)
    }
    fun checkedPermision() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )

        } else {
            var worker =
                PeriodicWorkRequestBuilder<locationWorker>(10, TimeUnit.SECONDS).build()
            WorkManager.getInstance(this).enqueue(worker)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkedPermision()
        } else {
            AlertDialog.Builder(this).apply {
                title = "warning"
                setMessage("To access location go to setting-> allow location service")
                setPositiveButton("Ok", { dialog, which ->


                })
            }.show()
        }

    }
}