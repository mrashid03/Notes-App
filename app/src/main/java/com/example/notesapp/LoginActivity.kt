package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.database.UserDatabaseHelper
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account != null){
            navigateToHome()
        }else{
            // Configure Google Sign-In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)

            val signInButton: Button = findViewById(R.id.googleBtn)
            signInButton.setOnClickListener {
                signIn()
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                // Successfully signed in, now you can save user data to SQLite
                saveUserData(account)

                displayUserData()

                navigateToHome()
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData(account: GoogleSignInAccount?) {
        account?.let {
            val userName = it.displayName ?: "Unknown"
            val userEmail = it.email ?: "Unknown"

            // Store user data in SQLite
            val dbHelper = UserDatabaseHelper(this)
            dbHelper.insertUserData(userName, userEmail)

            // Optionally, you can display the user's name or email on the UI
        }
    }


    private fun displayUserData() {
        val dbHelper = UserDatabaseHelper(this)
        val cursor = dbHelper.getUserData()

        if (cursor.moveToFirst()) {
            val nameColumnIndex = cursor.getColumnIndex(UserDatabaseHelper.COL_NAME)
            val emailColumnIndex = cursor.getColumnIndex(UserDatabaseHelper.COL_EMAIL)

            if (nameColumnIndex >= 0 && emailColumnIndex >= 0) {
                val userName = cursor.getString(nameColumnIndex)
                val userEmail = cursor.getString(emailColumnIndex)

                // You can display data or use it in your app
                println("User Name: $userName, Email: $userEmail")
            } else {
                // Handle the case where the column names are not found in the cursor
                println("Column(s) not found!")
            }
        }
        cursor.close()
    }

    private fun navigateToHome() {
        // Create an Intent to navigate to HomeActivity
        startActivity(Intent(this, HomeActivity::class.java))
        finish() // Optionally, call finish() to remove the current activity from the back stack
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
