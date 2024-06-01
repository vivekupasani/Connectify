package com.vivekupasani.connectify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.vivekupasani.connectify.R
import com.vivekupasani.connectify.models.Messages

class ChatAdapter(val context: Context, val messageList: ArrayList<Messages>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val SENDITEM = 1
    val RECEIVEITEM = 2

    class sendViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sendMsg = itemView.findViewById<TextView>(R.id.textviewSender)
    }

    class receiveViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMsg = itemView.findViewById<TextView>(R.id.textviewReceiver)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == SENDITEM){
            val view : View = LayoutInflater.from(context).inflate(R.layout.each_sender_design,parent,false)
            return sendViewHolder(view)
        }else{
            val view : View = LayoutInflater.from(context).inflate(R.layout.each_receiver_design,parent,false)
            return receiveViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessages = messageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessages.senderId)) {
            return SENDITEM
        } else {
            return RECEIVEITEM
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (holder.javaClass == sendViewHolder::class.java) {
            val viewHolder = holder as sendViewHolder
            holder.sendMsg.text = currentMessage.message

        } else {
            val viewHolder = holder as receiveViewHolder
            holder.receiveMsg.text = currentMessage.message
        }
    }
}