package com.example.trucking

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

// Activity class for handling the updating of truck information
class UpdateTruckActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_truck)

        // Retrieve references to the input fields in the layout
        val truckNameField = findViewById<EditText>(R.id.truck_name_input) // Field for truck name
        val locationNameField = findViewById<EditText>(R.id.location_name_input) // Field for location name
        val latitudeField = findViewById<EditText>(R.id.latitude_input) // Field for latitude input
        val longitudeField = findViewById<EditText>(R.id.longitude_input) // Field for longitude input

        // Retrieve reference to the progress bar in the layout
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.visibility = View.GONE // Initially hide the progress bar

        // Retrieve reference to the submit button in the layout
        val submitButton = findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener {
            val truckName = truckNameField.text.toString().trim() // Get and trim truck name input
            val locationName = locationNameField.text.toString().trim() // Get and trim location name input
            val latitude = latitudeField.text.toString().trim().toDoubleOrNull() // Convert latitude input to Double
            val longitude = longitudeField.text.toString().trim().toDoubleOrNull() // Convert longitude input to Double

            // Validate the user inputs for truck information
            when {
                truckName.isEmpty() -> {
                    Toast.makeText(this, "Truck name is required!", Toast.LENGTH_SHORT).show()
                }
                locationName.isEmpty() -> {
                    Toast.makeText(this, "Location name is required!", Toast.LENGTH_SHORT).show()
                }
                latitude == null || longitude == null -> {
                    Toast.makeText(this, "Invalid coordinates! Please enter valid latitude and longitude.", Toast.LENGTH_SHORT).show()
                }
                latitude !in -90.0..90.0 || longitude !in -180.0..180.0 -> {
                    Toast.makeText(this, "Coordinates out of range! Latitude must be between -90 and 90, Longitude between -180 and 180.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // If inputs are valid, proceed with updating the truck information
                    progressBar.visibility = View.VISIBLE // Show progress bar
                    checkAndUpdateTruck(truckName, locationName, latitude, longitude, progressBar)
                }
            }
        }
    }

    // Function to check and update the truck details in Firestore
    private fun checkAndUpdateTruck(
        truckName: String,         // Name of the truck
        locationName: String,      // Updated location name
        latitude: Double,          // Updated latitude value
        longitude: Double,         // Updated longitude value
        progressBar: ProgressBar   // Progress bar for loading indication
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val trucksCollection = firestore.collection("trucks")

        // Check if the truck already exists in Firestore
        trucksCollection.document(truckName).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Update the existing truck with new data
                    val updatedTruckData = mapOf(
                        "name" to truckName,          // Truck name
                        "location" to locationName,  // Updated location name
                        "geoPoint" to GeoPoint(latitude, longitude) // Updated geographical coordinates
                    )

                    trucksCollection.document(truckName).update(updatedTruckData)
                        .addOnSuccessListener {
                            // Inform user of successful update
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Truck updated successfully!", Toast.LENGTH_SHORT).show()
                            finish() // Close activity after update
                        }
                        .addOnFailureListener { e ->
                            // Inform user of failure to update
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Failed to update truck: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Create a new truck entry if it doesn't exist
                    val newTruckData = mapOf(
                        "name" to truckName,          // Truck name
                        "location" to locationName,  // Location name
                        "geoPoint" to GeoPoint(latitude, longitude) // Geographic coordinates
                    )

                    trucksCollection.document(truckName).set(newTruckData)
                        .addOnSuccessListener {
                            // Inform user of successful addition
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Truck added successfully!", Toast.LENGTH_SHORT).show()
                            finish() // Close activity after addition
                        }
                        .addOnFailureListener { e ->
                            // Inform user of failure to add the truck
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Failed to add truck: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                // Inform user of failure to check truck existence
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to check truck existence: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}