package com.example.trucking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// MainActivity: Represents the login screen of the app
// Allows users to log in with a username and password. Validates inputs and navigates to the homescreen on successful login.
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables edge-to-edge design for a modern UI experience
        enableEdgeToEdge()

        // Sets the layout for the login screen
        setContentView(R.layout.activity_main)

        // Apply padding dynamically to handle system bars (e.g., status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets // Return the insets to apply them
        }

        // Get references to input fields and button from the layout
        val usernameInput = findViewById<EditText>(R.id.username_input) // Input for the username
        val passwordInput = findViewById<EditText>(R.id.password_input) // Input for the password
        val loginButton = findViewById<Button>(R.id.login_button)       // Button to trigger the login process

        // Set an OnClickListener for the login button
        loginButton.setOnClickListener {
            val username = usernameInput.text.toString() // Get the entered username
            val password = passwordInput.text.toString() // Get the entered password

            // Validate the input fields to ensure they are not empty
            if (username.isEmpty() || password.isEmpty()) {
                // Display a Toast message if either field is empty
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {
                // Simple login processing logic for demonstration purposes
                if (username == "admin" && password == "1234") {
                    // Display a success message and navigate to the homescreen
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, homescreen::class.java) // Create intent to navigate
                    intent.putExtra("USERNAME", username)             // Pass the username to the homescreen
                    startActivity(intent)                             // Start the new activity
                } else {
                    // Display an error message for invalid credentials
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
