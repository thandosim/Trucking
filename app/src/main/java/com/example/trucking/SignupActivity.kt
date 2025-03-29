package com.example.trucking

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Activity class for handling user sign-up
class SignupActivity : AppCompatActivity() {

    // Firebase Authentication instance for managing user accounts
    private lateinit var auth: FirebaseAuth

    // Firebase Firestore instance for saving user data
    private lateinit var firestore: FirebaseFirestore

    // Progress bar to indicate loading during sign-up process
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase services
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Get references to the input fields, button, and progress bar in the layout
        val emailInput = findViewById<EditText>(R.id.email_input) // Email input field
        val passwordInput = findViewById<EditText>(R.id.password_input) // Password input field
        val signupButton = findViewById<Button>(R.id.submit_signup_button) // Sign-up button
        progressBar = findViewById(R.id.progress_bar) // Progress bar for loading indication

        // Set up the click listener for the sign-up button
        signupButton.setOnClickListener {
            val email = emailInput.text.toString().trim() // Retrieve and trim the email input
            val password = passwordInput.text.toString() // Retrieve the password input

            // Input validation: Check if email or password is empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Input validation: Ensure the email format is valid
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Input validation: Ensure the password meets minimum length requirement
            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Display the progress bar while the sign-up process is ongoing
            progressBar.visibility = ProgressBar.VISIBLE

            // Sign up the user using Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    // Hide the progress bar after the sign-up process is complete
                    progressBar.visibility = ProgressBar.GONE

                    // Check if the sign-up task was successful
                    if (task.isSuccessful) {
                        // Create a user object to save in Firestore
                        val user = hashMapOf(
                            "email" to email, // Save user's email
                            "signupTime" to System.currentTimeMillis() // Record the sign-up time
                        )

                        // Save the user's data in the Firestore database
                        firestore.collection("users").document(email)
                            .set(user)
                            .addOnSuccessListener {
                                // Inform the user that sign-up was successful
                                Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show()
                                finish() // Navigate back to the login screen
                            }
                            .addOnFailureListener { e ->
                                // Handle and display Firestore save failure
                                Toast.makeText(this, "Error saving user data: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        // Handle and display Firebase Authentication failure
                        val errorMessage = task.exception?.localizedMessage ?: "Sign-up failed"
                        Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
