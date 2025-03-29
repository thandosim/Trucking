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
data class Truck(
    val name: String,
    val locationName: String,
    val geoPoint: GeoPoint
)

// Adapter for displaying a list of trucks
class TruckAdapter(
    private val context: Context, // Required for launching activities
    private var trucks: List<Truck>, // Current truck list
    private val onItemClick: (Truck) -> Unit, // Callback for item click
    private val onTruckDeleted: (Truck) -> Unit // Callback for notifying the activity when a truck is deleted
) : RecyclerView.Adapter<TruckAdapter.TruckViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TruckViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_truck, parent, false)
        return TruckViewHolder(view)
    }

    override fun onBindViewHolder(holder: TruckViewHolder, position: Int) {
        val truck = trucks[position]
        Log.d("TruckAdapter", "Binding truck: ${truck.name}, Location: ${truck.locationName}, Coordinates: Lat: ${truck.geoPoint.latitude}, Long: ${truck.geoPoint.longitude}")

        holder.bind(truck)

        // Handle delete button click
        holder.deleteButton.setOnClickListener {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("trucks").document(truck.name)
                .delete()
                .addOnSuccessListener {
                    Log.d("TruckAdapter", "Truck ${truck.name} deleted successfully.")
                    trucks = trucks.filter { it.name != truck.name }
                    notifyDataSetChanged() // Notify RecyclerView
                    onTruckDeleted(truck) // Notify FindTrucks activity for MapView updates
                }
                .addOnFailureListener { e ->
                    Log.e("TruckAdapter", "Failed to delete truck ${truck.name}: ${e.message}")
                }
        }

        // Handle update button click
        holder.updateButton.setOnClickListener {
            val intent = Intent(context, UpdateTruckActivity::class.java)
            intent.putExtra("TRUCK_NAME", truck.name)
            intent.putExtra("LOCATION_NAME", truck.locationName)
            intent.putExtra("LATITUDE", truck.geoPoint.latitude)
            intent.putExtra("LONGITUDE", truck.geoPoint.longitude)
            context.startActivity(intent)
        }

        // Handle general item click (optional callback)
        holder.itemView.setOnClickListener { onItemClick(truck) }
    }

    override fun getItemCount(): Int {
        Log.d("TruckAdapter", "RecyclerView item count: ${trucks.size}")
        return trucks.size
    }

    // Efficient update using DiffUtil
    fun updateTrucks(newTrucks: List<Truck>) {
        Log.d("TruckAdapter", "Updating adapter with ${newTrucks.size} trucks")
        val diffCallback = TruckDiffCallback(trucks, newTrucks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        trucks = newTrucks
        diffResult.dispatchUpdatesTo(this) // Notify RecyclerView of updates
    }

    // ViewHolder class for truck items
    class TruckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.truck_name_text)
        private val locationTextView: TextView = itemView.findViewById(R.id.truck_location_text)
        private val coordinatesTextView: TextView = itemView.findViewById(R.id.truck_geopoint_text)
        val deleteButton: Button = itemView.findViewById(R.id.button_delete)
        val updateButton: Button = itemView.findViewById(R.id.button_update)

        fun bind(truck: Truck) {
            nameTextView.text = truck.name
            locationTextView.text = "Location: ${truck.locationName}"
            coordinatesTextView.text = "Lat: ${truck.geoPoint.latitude}, Long: ${truck.geoPoint.longitude}"
        }
    }
}

// DiffUtil Callback for efficient updates
class TruckDiffCallback(
    private val oldList: List<Truck>,
    private val newList: List<Truck>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        Log.d("TruckDiffCallback", "Old list size: ${oldList.size}")
        return oldList.size
    }

    override fun getNewListSize(): Int {
        Log.d("TruckDiffCallback", "New list size: ${newList.size}")
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        val areSame = oldItem.name == newItem.name
        Log.d("TruckDiffCallback", "Are items the same: $areSame")
        return areSame
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        val areContentsSame = oldItem == newItem
        Log.d("TruckDiffCallback", "Are contents the same: $areContentsSame")
        return areContentsSame
    }
}
