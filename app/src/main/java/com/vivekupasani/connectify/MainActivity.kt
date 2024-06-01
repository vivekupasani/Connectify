package com.vivekupasani.connectify

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vivekupasani.connectify.adapters.MyAdapter
import com.vivekupasani.connectify.models.Users

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var firebase: DatabaseReference
    lateinit var contactRecyclerView: RecyclerView
    lateinit var userList: ArrayList<Users>
    lateinit var welcomeName: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        firebase = FirebaseDatabase.getInstance().getReference()

        welcomeName = findViewById(R.id.welcomeusername)
        welcomeName.text = FirebaseAuth.getInstance().currentUser?.email

        contactRecyclerView = findViewById(R.id.homeRecyclerView)
        contactRecyclerView.layoutManager = LinearLayoutManager(this)
        userList = ArrayList()
        val adapter = MyAdapter(this, userList)
        contactRecyclerView.adapter = adapter

        adapter.setOnclickListner(object : MyAdapter.setOnclickListner {
            override fun setonclick(position: Int) {
                val intent = Intent(this@MainActivity, ChattingActivity::class.java)
                intent.putExtra("name", userList[position].name)
                intent.putExtra("receiverId", userList[position].uid)
                startActivity(intent)

            }
        })


        firebase.child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                if (snapshot.exists()) {
                    for (eachSnap in snapshot.children) {
                        val snapData = eachSnap.getValue(Users::class.java)
                        if (snapData?.email != auth.currentUser?.email) {
                            userList.add(snapData!!)
                        }

                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    //Actionbar menu settings
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuLogout -> {
                auth.signOut()
                val intent = Intent(this, signupActivity::class.java)
                startActivity(intent)
                true
            }

            else -> false

        }
        return true
    }
}