package com.alidevs.locationservice

import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alidevs.locationservice.databinding.LocationRowBinding
import java.text.DateFormat
import java.util.*

class LocationsAdapter : RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {

	private lateinit var binding: LocationRowBinding
	private val locations = mutableListOf<LocationModel>()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		binding = LocationRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(binding.root)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(locations[position])
		holder.setIsRecyclable(false)
	}

	override fun getItemCount(): Int {
		return locations.size
	}

	fun addLocation(location: LocationModel) {
		locations.add(location)
		notifyDataSetChanged()
	}

	fun submitList(list: List<LocationModel>) {
		locations.clear()
		locations.addAll(list)
		notifyDataSetChanged()
	}

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(location: LocationModel) {
			binding.longitudeTextView.text = location.longitude.toString()
			binding.latitudeTextView.text = location.latitude.toString()
			binding.timeTextView.text = location.time.toDateString()
			binding.locationNameTextView.text =
				location.getLocationName(location.latitude, location.longitude)
		}

		private fun LocationModel.getLocationName(latitude: Double, longitude: Double): String {
			val gcd = Geocoder(itemView.context, Locale.getDefault())
			val addresses: List<Address> = gcd.getFromLocation(latitude, longitude, 1)
			if (addresses.isNotEmpty() && addresses[0].locality != null) {
				return addresses[0].locality
			}
			return "Unknown"
		}
	}

	// Long extension to convert Long to date string format mmm dd, hh:mm:ss
	fun Long.toDateString(dateFormat: Int = DateFormat.MEDIUM): String {
		val df = DateFormat.getDateTimeInstance(dateFormat, DateFormat.SHORT)
		return df.format(this)
	}
}

