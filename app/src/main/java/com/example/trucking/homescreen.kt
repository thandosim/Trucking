package com.example.trucking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class homescreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_homescreen)

        // Handle system bars insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get username from intent
        val username = intent.getStringExtra("USERNAME") ?: "Guest"
        Toast.makeText(this, "Welcome, $username!", Toast.LENGTH_SHORT).show()

        // Handle Find Trucks button click
        val findTrucksButton = findViewById<Button>(R.id.find_trucks_button)
        findTrucksButton.setOnClickListener {
            val intent = Intent(this, FindTrucks::class.java)
            startActivity(intent)
        }

        // Find and set up the "Find Cargo" button
        val findCargoButton = findViewById<Button>(R.id.find_cargo_button)
        findCargoButton.setOnClickListener {
            Toast.makeText(this, "The 'Find Your Cargo' feature is coming soon!", Toast.LENGTH_LONG).show()
        }
    }
}
