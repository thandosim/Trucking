package com.example.trucking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountManagementActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_management)

        // Initialize Firebase Authentication and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Get references to input fields and buttons
        val properNameInput = findViewById<EditText>(R.id.proper_name_input)
        val changePasswordInput = findViewById<EditText>(R.id.new_password_input)
        val addNameButton = findViewById<Button>(R.id.add_name_button)
        val changePasswordButton = findViewById<Button>(R.id.change_password_button)
        val deleteAccountButton = findViewById<Button>(R.id.delete_account_button)

        // Add proper name (identifier) to the database
        addNameButton.setOnClickListener {
            val properName = properNameInput.text.toString()
            val user = auth.currentUser

            if (user != null && properName.isNotEmpty()) {
                val email = user.email ?: "unknown"
                firestore.collection("users").document(email)
                    .update("properName", properName)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Name added successfully!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error adding name: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please enter a name or sign in.", Toast.LENGTH_SHORT).show()
            }
        }

        // Change account password
        changePasswordButton.setOnClickListener {
            val newPassword = changePasswordInput.text.toString()
            val user = auth.currentUser

            if (user != null && newPassword.isNotEmpty() && newPassword.length >= 6) {
                user.updatePassword(newPassword)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error changing password: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please enter a valid password.", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete account from the database
        deleteAccountButton.setOnClickListener {
            val user = auth.currentUser
            if (user != null) {
                val email = user.email ?: "unknown"
                firestore.collection("users").document(email)
                    .delete()
                    .addOnSuccessListener {
                        user.delete()
                            .addOnSuccessListener {
                                Toast.makeText(this, "Account deleted successfully!", Toast.LENGTH_SHORT).show()
                                // Navigate to the login screen after deleting the account
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear activity stack
                                startActivity(intent)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error deleting account: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error deleting account from Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "No user signed in.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
