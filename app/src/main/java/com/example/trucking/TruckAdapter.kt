package com.example.trucking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.GeoPoint

// Data model for a Truck
// Represents a truck with a name, location description, and geographical coordinates.
data class Truck(
    val name: String,        // The name of the truck
    val locationName: String, // A readable name or description of the truck's location
    val geoPoint: GeoPoint    // Geographic coordinates (latitude and longitude) of the truck
)

// Adapter for displaying a list of Truck objects in a RecyclerView
class TruckAdapter(private val trucks: List<Truck>) : RecyclerView.Adapter<TruckAdapter.TruckViewHolder>() {

    // ViewHolder: Manages and recycles the views for each item in the RecyclerView
    class TruckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val truckName: TextView = itemView.findViewById(R.id.truck_name) // Displays the truck's name
        val truckLocationName: TextView = itemView.findViewById(R.id.truck_location) // Displays the location description
        val truckCoordinates: TextView = itemView.findViewById(R.id.truck_coordinates) // Displays latitude and longitude
    }

    // Called when a new ViewHolder is created (inflates the item layout)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TruckViewHolder {
        // Inflate the layout XML for each truck item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_truck, parent, false) // Custom layout for the list item
        return TruckViewHolder(itemView)
    }

    // Binds data from the Truck object to the views in the ViewHolder
    override fun onBindViewHolder(holder: TruckViewHolder, position: Int) {
        val truck = trucks[position] // Get the Truck object for the current position
        holder.truckName.text = truck.name // Set the truck name in the TextView
        holder.truckLocationName.text = truck.locationName // Set the location name in the TextView
        holder.truckCoordinates.text = "Lat: ${truck.geoPoint.latitude}, Lng: ${truck.geoPoint.longitude}"
        // Sets the coordinates in the TextView
    }

    // Returns the total number of Truck items in the list
    override fun getItemCount(): Int {
        return trucks.size // The size of the truck list
    }
}
