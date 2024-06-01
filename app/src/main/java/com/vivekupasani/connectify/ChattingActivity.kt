package com.vivekupasani.connectify

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.vivekupasani.connectify.adapters.ChatAdapter
import com.vivekupasani.connectify.models.Messages

@Suppress("DEPRECATION")
class ChattingActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var firebase: DatabaseReference
    lateinit var msgBox: TextInputEditText
    lateinit var send: ImageView
    lateinit var messageList: ArrayList<Messages>
    lateinit var contactView : RecyclerView
    lateinit var contactAdapter: ChatAdapter
    var senderRoom: String? = null
    var receiverRoom: String? = null
//github
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chatting)

        auth = FirebaseAuth.getInstance()
        firebase = FirebaseDatabase.getInstance().reference

        val useRname = intent.getStringExtra("name")
        val receiverId = intent.getStringExtra("receiverId")
        val senderId = auth.currentUser?.uid
        senderRoom = senderId + receiverId
        receiverRoom = receiverId + senderId

        supportActionBar?.title = useRname
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable back button

        msgBox = findViewById(R.id.MessageBox)
        send = findViewById(R.id.btnSend)
        messageList = ArrayList()

        contactView = findViewById(R.id.caRecyclerView)
        contactView.layoutManager = LinearLayoutManager(this)
        contactView.setHasFixedSize(false)
        contactAdapter = ChatAdapter(this, messageList)
        contactView.adapter = contactAdapter

        firebase.child("Chats").child(senderRoom!!).child("messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                if (snapshot.exists()) {
                    for (eachSnap in snapshot.children) {
                        val dataSnap = eachSnap.getValue(Messages::class.java)
                        messageList.add(dataSnap!!)
                    }
                    contactAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })

        send.setOnClickListener {
            val msgB = msgBox.text.toString()
            if (msgB.isEmpty()) {
                Toast.makeText(this, "Blank Message box", Toast.LENGTH_SHORT).show()
            } else {
                firebase.child("Chats").child(senderRoom!!).child("messages").push()
                    .setValue(Messages(msgB, senderId)).addOnSuccessListener {
                        firebase.child("Chats").child(receiverRoom!!).child("messages").push()
                            .setValue(Messages(msgB, senderId))
                    }

                msgBox.text?.clear()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // Handle back button click
                finish()
                true
            }
            R.id.menuLogout -> {
                auth.signOut()
                val intent = Intent(this, signupActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
