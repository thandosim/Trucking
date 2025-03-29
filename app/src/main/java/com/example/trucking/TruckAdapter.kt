package com.example.trucking

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

// Data model for a Truck
// Represents the details of a truck including its name, location, and geographical coordinates.
data class Truck(
    val name: String,              // Truck name
    val locationName: String,      // Location name associated with the truck
    val geoPoint: GeoPoint         // Geographic coordinates of the truck
)

// Adapter for displaying a list of trucks in a RecyclerView
class TruckAdapter(
    private val context: Context,                // Context required for launching activities
    private var trucks: List<Truck>,             // List of trucks to display
    private val onItemClick: (Truck) -> Unit,    // Callback for handling item clicks
    private val onTruckDeleted: (Truck) -> Unit  // Callback for notifying the activity when a truck is deleted
) : RecyclerView.Adapter<TruckAdapter.TruckViewHolder>() {

    // Creates a new ViewHolder for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TruckViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_truck, parent, false)
        return TruckViewHolder(view)
    }

    // Binds the data of a truck to the ViewHolder
    override fun onBindViewHolder(holder: TruckViewHolder, position: Int) {
        val truck = trucks[position]
        Log.d("TruckAdapter", "Binding truck: ${truck.name}, Location: ${truck.locationName}, Coordinates: Lat: ${truck.geoPoint.latitude}, Long: ${truck.geoPoint.longitude}")

        holder.bind(truck)

        // Handles the delete button click event
        holder.deleteButton.setOnClickListener {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("trucks").document(truck.name)
                .delete()
                .addOnSuccessListener {
                    Log.d("TruckAdapter", "Truck ${truck.name} deleted successfully.")
                    trucks = trucks.filter { it.name != truck.name } // Updates the list locally
                    notifyDataSetChanged() // Notifies RecyclerView of the changes
                    onTruckDeleted(truck) // Notifies the activity to update the MapView
                }
                .addOnFailureListener { e ->
                    Log.e("TruckAdapter", "Failed to delete truck ${truck.name}: ${e.message}")
                }
        }

        // Handles the update button click event
        holder.updateButton.setOnClickListener {
            val intent = Intent(context, UpdateTruckActivity::class.java).apply {
                putExtra("TRUCK_NAME", truck.name)
                putExtra("LOCATION_NAME", truck.locationName)
                putExtra("LATITUDE", truck.geoPoint.latitude)
                putExtra("LONGITUDE", truck.geoPoint.longitude)
            }
            context.startActivity(intent)
        }

        // Handles general item click, if provided as an optional callback
        holder.itemView.setOnClickListener { onItemClick(truck) }
    }

    // Returns the total number of trucks in the list
    override fun getItemCount(): Int {
        Log.d("TruckAdapter", "RecyclerView item count: ${trucks.size}")
        return trucks.size
    }

    // Updates the truck list efficiently using DiffUtil
    fun updateTrucks(newTrucks: List<Truck>) {
        Log.d("TruckAdapter", "Updating adapter with ${newTrucks.size} trucks")
        val diffCallback = TruckDiffCallback(trucks, newTrucks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        trucks = newTrucks
        diffResult.dispatchUpdatesTo(this) // Notifies RecyclerView of changes
    }

    // ViewHolder class for displaying a truck item
    class TruckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.truck_name_text)           // Displays truck name
        private val locationTextView: TextView = itemView.findViewById(R.id.truck_location_text)   // Displays truck location name
        private val coordinatesTextView: TextView = itemView.findViewById(R.id.truck_geopoint_text)// Displays truck coordinates
        val deleteButton: Button = itemView.findViewById(R.id.button_delete)                      // Delete button
        val updateButton: Button = itemView.findViewById(R.id.button_update)                      // Update button

        // Binds the truck's data to the ViewHolder's views
        fun bind(truck: Truck) {
            nameTextView.text = truck.name
            locationTextView.text = "Location: ${truck.locationName}"
            coordinatesTextView.text = "Lat: ${truck.geoPoint.latitude}, Long: ${truck.geoPoint.longitude}"
        }
    }
}

// DiffUtil Callback for efficiently comparing two lists of trucks
class TruckDiffCallback(
    private val oldList: List<Truck>, // Old truck list
    private val newList: List<Truck>  // New truck list
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        Log.d("TruckDiffCallback", "Old list size: ${oldList.size}")
        return oldList.size
    }

    override fun getNewListSize(): Int {
        Log.d("TruckDiffCallback", "New list size: ${newList.size}")
        return newList.size
    }

    // Checks if the items at the same position in both lists represent the same truck
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        val areSame = oldItem.name == newItem.name
        Log.d("TruckDiffCallback", "Are items the same: $areSame")
        return areSame
    }

    // Checks if the contents of the trucks at the same position are identical
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        val areContentsSame = oldItem == newItem
        Log.d("TruckDiffCallback", "Are contents the same: $areContentsSame")
        return areContentsSame
    }
}