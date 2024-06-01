package com.vivekupasani.connectify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.vivekupasani.connectify.models.Users

class signupActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var firebase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        supportActionBar?.hide()

        val Gologin = findViewById<TextView>(R.id.GOLOGIN)
        Gologin.setOnClickListener {
            val intent = Intent(this, signInActivity::class.java)
            startActivity(intent)
        }

        val getName = findViewById<TextInputEditText>(R.id.etName)
        val getEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val getpassword = findViewById<TextInputEditText>(R.id.etPassword)
        val getsignup = findViewById<Button>(R.id.btnSignup)

        auth = Firebase.auth
        firebase = FirebaseDatabase.getInstance().reference


        getsignup.setOnClickListener {
            val gotName = getName.text.toString()
            val gotEmail = getEmail.text.toString()
            val gotPassword = getpassword.text.toString()
            auth.createUserWithEmailAndPassword(gotEmail, gotPassword).addOnCompleteListener {
                if (it.isSuccessful) {

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    val uid = auth.currentUser?.uid
                    storeDataIntoRealtimeDatabase(gotName,gotEmail,uid!!)
                    Log.d("signUp", "User registration successful for email: $gotEmail")

                } else {
                    Log.e("signUp", "User registration failed for email: $gotEmail", it.exception)
                }

            }
        }
    }

    private fun storeDataIntoRealtimeDatabase(gotName: String,gotEmail: String, uid: String) {
        firebase.child("Users").child(uid).setValue(Users(gotName,gotEmail,uid)).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("signUp", "User registration successful for email: $gotEmail")
            } else {
                Log.e("signUp", "User registration failed for email: $gotEmail", it.exception)
            }
        }
    }



}