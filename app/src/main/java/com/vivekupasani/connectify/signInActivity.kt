package com.vivekupasani.connectify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class signInActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var gettingEmail: TextInputEditText
    lateinit var gettingPassword: TextInputEditText
    lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)

        supportActionBar?.hide()


        val GoSignup = findViewById<TextView>(R.id.GOSIGNUP)
        GoSignup.setOnClickListener {
            val intent = Intent(this, signupActivity::class.java)
            startActivity(intent)
        }

        gettingEmail = findViewById(R.id.etlogEmail)
        gettingPassword = findViewById(R.id.etlogPass)
        btnLogin = findViewById(R.id.btnSignin)

        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            val gMail = gettingEmail.text.toString()
            val gPass = gettingPassword.text.toString()

            if (gMail.isNotEmpty() && gPass.isNotEmpty()) {
                auth.signInWithEmailAndPassword(gMail, gPass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        Log.d("signIn", "User login successful for email: $gMail")
                    } else {
                        Log.e("signIn", "User login failed for email: $gMail", task.exception)
                    }
                }
            } else {
                Log.e("signIn", "Email or password is empty")
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}