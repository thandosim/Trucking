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

class UpdateTruckActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_truck)

        // Input fields
        val truckNameField = findViewById<EditText>(R.id.truck_name_input)
        val locationNameField = findViewById<EditText>(R.id.location_name_input)
        val latitudeField = findViewById<EditText>(R.id.latitude_input)
        val longitudeField = findViewById<EditText>(R.id.longitude_input)

        // Progress bar
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.visibility = View.GONE

        // Submit button
        val submitButton = findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener {
            val truckName = truckNameField.text.toString().trim()
            val locationName = locationNameField.text.toString().trim()
            val latitude = latitudeField.text.toString().trim().toDoubleOrNull()
            val longitude = longitudeField.text.toString().trim().toDoubleOrNull()

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
                    progressBar.visibility = View.VISIBLE
                    checkAndUpdateTruck(truckName, locationName, latitude, longitude, progressBar)
                }
            }
        }
    }

    private fun checkAndUpdateTruck(
        truckName: String,
        locationName: String,
        latitude: Double,
        longitude: Double,
        progressBar: ProgressBar
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val trucksCollection = firestore.collection("trucks")

        // Check if a truck with the given name exists
        trucksCollection.document(truckName).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Truck exists, update it
                    val updatedTruckData = mapOf(
                        "name" to truckName,
                        "location" to locationName,
                        "geoPoint" to GeoPoint(latitude, longitude)
                    )

                    trucksCollection.document(truckName).update(updatedTruckData)
                        .addOnSuccessListener {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Truck updated successfully!", Toast.LENGTH_SHORT).show()
                            finish() // Close the activity after a successful update
                        }
                        .addOnFailureListener { e ->
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Failed to update truck: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Truck doesn't exist, create a new one
                    val newTruckData = mapOf(
                        "name" to truckName,
                        "location" to locationName,
                        "geoPoint" to GeoPoint(latitude, longitude)
                    )

                    trucksCollection.document(truckName).set(newTruckData)
                        .addOnSuccessListener {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Truck added successfully!", Toast.LENGTH_SHORT).show()
                            finish() // Close the activity after a successful addition
                        }
                        .addOnFailureListener { e ->
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Failed to add truck: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to check truck existence: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
