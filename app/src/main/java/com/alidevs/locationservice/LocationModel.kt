package com.alidevs.locationservice

import android.location.Address
import android.location.Geocoder
import java.util.*


data class LocationModel(
	var latitude: Double = 0.0,
	var longitude: Double = 0.0,
	var altitude: Double = 0.0,
	var accuracy: Float = 0.0f,
	var time: Long= 0
)