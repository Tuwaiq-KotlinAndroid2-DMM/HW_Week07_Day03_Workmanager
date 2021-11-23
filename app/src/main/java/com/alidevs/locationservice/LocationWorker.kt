package com.alidevs.locationservice

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore

class LocationWorker(context: Context, workerParams: WorkerParameters) :
	Worker(context, workerParams) {

	private lateinit var locationManager: LocationManager

	@SuppressLint("MissingPermission")
	override fun doWork(): Result {
		locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
		val db = FirebaseFirestore.getInstance()

		val location = getLocation() ?: return Result.failure()

		val locationModel = LocationModel(
			location.latitude,
			location.longitude,
			location.altitude,
			location.accuracy,
			location.time
		)

		// Add location to repository
		LocationRepository
			.getInstance(db)
			.addLocation(locationModel)

		return Result.success()
	}

	@SuppressLint("MissingPermission")
	fun getLocation(): Location? {
		return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
	}

}