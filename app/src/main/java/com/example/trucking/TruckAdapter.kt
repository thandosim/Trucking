package com.example.trucking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.GeoPoint

// Data model for a Truck
data class Truck(
    val name: String,
    val locationName: String, // Example: "Location A"
    val geoPoint: GeoPoint // Location of the truck (latitude and longitude)
)

class TruckAdapter(private val trucks: List<Truck>) : RecyclerView.Adapter<TruckAdapter.TruckViewHolder>() {

    // ViewHolder: Represents each item in the list
    class TruckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val truckName: TextView = itemView.findViewById(R.id.truck_name)
        val truckLocationName: TextView = itemView.findViewById(R.id.truck_location)
        val truckCoordinates: TextView = itemView.findViewById(R.id.truck_coordinates) // New TextView for coordinates
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TruckViewHolder {
        // Inflate the layout for each list item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_truck, parent, false)
        return TruckViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TruckViewHolder, position: Int) {
        // Bind data to the list item views
        val truck = trucks[position]
        holder.truckName.text = truck.name
        holder.truckLocationName.text = truck.locationName
        holder.truckCoordinates.text = "Lat: ${truck.geoPoint.latitude}, Lng: ${truck.geoPoint.longitude}" // Display coordinates
    }

    override fun getItemCount(): Int {
        // Return the total number of items in the data list
        return trucks.size
    }
}
