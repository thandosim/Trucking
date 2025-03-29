package com.example.trucking

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Get references to input fields, button, and progress bar
        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val signupButton = findViewById<Button>(R.id.submit_signup_button)
        progressBar = findViewById(R.id.progress_bar) // Ensure this is added to your layout

        // Set OnClickListener for the Sign-Up button
        signupButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()

            // Input Validation
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show Progress Bar while signing up
            progressBar.visibility = ProgressBar.VISIBLE

            // Firebase sign-up process
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    progressBar.visibility = ProgressBar.GONE // Hide Progress Bar

                    if (task.isSuccessful) {
                        val user = hashMapOf(
                            "email" to email,
                            "signupTime" to System.currentTimeMillis()
                        )
                        firestore.collection("users").document(email)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show()
                                finish() // Go back to the login screen
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error saving user data: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        val errorMessage = task.exception?.localizedMessage ?: "Sign-up failed"
                        Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
