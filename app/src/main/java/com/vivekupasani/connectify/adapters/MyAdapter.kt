package com.vivekupasani.connectify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vivekupasani.connectify.R
import com.vivekupasani.connectify.models.Users

class MyAdapter(val context: Context,val userList : ArrayList<Users>) : RecyclerView.Adapter<MyAdapter.viewHolder>() {

    lateinit var mlistner : setOnclickListner
    interface setOnclickListner{
        fun setonclick(position: Int)
    }
    fun setOnclickListner(listner : setOnclickListner){
        mlistner = listner
    }
    class viewHolder(val itemView : View,listner : setOnclickListner) : RecyclerView.ViewHolder(itemView) {
                val name = itemView.findViewById<TextView>(R.id.clName)
        init {
            itemView.setOnClickListener {
                listner.setonclick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
     val itemView = LayoutInflater.from(context).inflate(R.layout.each_contact_in_list,parent,false)
        return viewHolder(itemView,mlistner)
    }

    override fun getItemCount(): Int {
      return userList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
      var currentUser = userList[position]
        holder.name.text = currentUser.name
    }
}