package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.work.*
import com.google.firebase.firestore.FirebaseFirestore

class LocationWorker constructor(context: Context, workerParams: WorkerParameters) :
	Worker(context, workerParams) {

	@SuppressLint("MissingPermission")
	override fun doWork(): Result {
		var locationManager =
			applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
		var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

		val locationMap = hashMapOf(
			"lat" to location!!.latitude,
			"lon" to location.longitude,
			"accuracy" to location.accuracy
		)

		Log.d("Firebase", "Hola1")

		val db = FirebaseFirestore.getInstance()
		db.collection("locations").add(locationMap)
			.addOnSuccessListener {
				Log.i("Firebase", "Success: $it")
			}
			.addOnFailureListener {
				Log.e("Firebase", "Failure: $it")
			}

		return Result.success()
	}

}