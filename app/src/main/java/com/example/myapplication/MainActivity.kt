package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

	@SuppressLint("RestrictedApi")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		checkPermissions()

		val locationWorker = PeriodicWorkRequestBuilder<LocationWorker>(5, TimeUnit.SECONDS).build()

		WorkManager.getInstance(this).enqueue(locationWorker)
		Log.d("MainActivity", "onCreate")
	}

	// Ask user for location permission if not granted
	fun checkPermissions() {
		Log.d("Firebase", "Hola")
		if (ActivityCompat.checkSelfPermission(
				applicationContext,
				Manifest.permission.ACCESS_FINE_LOCATION
			) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
				applicationContext,
				Manifest.permission.ACCESS_COARSE_LOCATION
			) != PackageManager.PERMISSION_GRANTED
		) {
			ActivityCompat.requestPermissions(
				this,
				arrayOf(
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.ACCESS_COARSE_LOCATION
				),
				1
			)
		}
	}
}