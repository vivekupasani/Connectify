package com.vivekupasani.connectify

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class splashscreen : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splashscreen)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        auth = Firebase.auth

        val currentUser = auth.currentUser?.uid

        if (currentUser != null){
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            },3000)
        }else{
            Handler().postDelayed({
                val intent = Intent(this, signupActivity::class.java)
                startActivity(intent)
                finish()
            },3000)
        }


    }
}