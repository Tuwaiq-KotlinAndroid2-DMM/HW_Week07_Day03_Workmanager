package com.alidevs.locationservice

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class LocationRepository constructor(val db: FirebaseFirestore) {

	private val locations = MutableLiveData<List<LocationModel>>()

	companion object {
		fun getInstance(db: FirebaseFirestore) = LocationRepository(db)
	}

	fun getLocations(): MutableLiveData<List<LocationModel>> {
		getLocationsFromFirebase()
		return locations
	}

	fun addLocation(location: LocationModel) {
		db
			.collection("locations")
			.add(location)
			.addOnSuccessListener {
				Log.d("LocationRepository", "DocumentSnapshot added with ID: ${it.id}")
			}
			.addOnFailureListener {
				Log.w("LocationRepository", "Error adding document", it)
			}
	}

	private fun getLocationsFromFirebase() {
		db
			.collection("locations")
			.orderBy("time", com.google.firebase.firestore.Query.Direction.DESCENDING)
			.get()
			.addOnSuccessListener {
				locations.value = it.toObjects(LocationModel::class.java)
				Log.d("LocationRepository", "Successfully got locations from firebase")
			}
			.addOnFailureListener {
				Log.d("LocationRepository", "Failed to get locations from firebase")
			}
	}
}
