package com.example.trucking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// Activity: Homescreen
// Represents the main landing screen of the application, providing options to navigate to
// "Find Trucks", "Find Your Cargo", "Update Locations", and "Manage Account" features.
class homescreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables edge-to-edge system UI styling for immersive design
        enableEdgeToEdge()

        // Sets the layout for the homescreen activity
        setContentView(R.layout.activity_homescreen)

        // Handle system bars (e.g., status bar, navigation bar) to adjust padding dynamically
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets // Return the insets to apply them
        }

        // Retrieve the username passed via the intent
        val username = intent.getStringExtra("USERNAME") ?: "Guest" // Default to "Guest" if no username is provided

        // Display a welcome message with the retrieved username
        Toast.makeText(this, "Welcome, $username!", Toast.LENGTH_SHORT).show()

        // Initialize and set up the "Find Trucks" button
        val findTrucksButton = findViewById<Button>(R.id.find_trucks_button)
        findTrucksButton.setOnClickListener {
            // Navigate to the FindTrucks activity when the button is clicked
            val intent = Intent(this, FindTrucks::class.java)
            startActivity(intent)
        }

        // Initialize and set up the "Find Cargo" button
        val findCargoButton = findViewById<Button>(R.id.find_cargo_button)
        findCargoButton.setOnClickListener {
            // Show a Toast message indicating that the feature is not yet implemented
            Toast.makeText(this, "The 'Find Your Cargo' feature is coming soon!", Toast.LENGTH_LONG).show()
        }

        // Initialize and set up the "Update Truck Location" button
        val updateTruckButton = findViewById<Button>(R.id.update_truck_location_button)
        updateTruckButton.setOnClickListener {
            // Navigate to the UpdateTruck activity when the button is clicked
            val intent = Intent(this, UpdateTruckActivity::class.java)
            startActivity(intent)
        }

        // Initialize and set up the "Update Cargo Location" button
        val updateCargoButton = findViewById<Button>(R.id.update_cargo_location_button)
        updateCargoButton.setOnClickListener {
            // Show a Toast message indicating that the feature is not yet implemented
            Toast.makeText(this, "The 'Update Cargo Location' feature is coming soon!", Toast.LENGTH_LONG).show()
        }

        // Initialize and set up the "Manage Account" button
        val manageAccountButton = findViewById<Button>(R.id.manage_account_button)
        manageAccountButton.setOnClickListener {
            // Navigate to the AccountManagement activity when the button is clicked
            val intent = Intent(this, AccountManagementActivity::class.java)
            startActivity(intent)
        }
    }
}
