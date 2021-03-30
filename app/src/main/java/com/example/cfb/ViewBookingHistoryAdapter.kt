package com.example.cfb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.core.content.ContextCompat.startActivity


import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase
import org.jetbrains.anko.backgroundColor


class ViewBookingHistoryAdapter(var context: Context, var bookingList: MutableList<BookingHistory>):
    RecyclerView.Adapter<ViewBookingHistoryAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var slot: TextView = itemView.findViewById(R.id.slot)
        var name: TextView = itemView.findViewById(R.id.name)
        var date: TextView = itemView.findViewById(R.id.date)
        var building: TextView = itemView.findViewById(R.id.buildingname)


    }
    override fun onBindViewHolder(holder: ViewBookingHistoryAdapter.DetailsViewHolder, position: Int) {

        holder.name.text = bookingList[position].facilityName
        holder.building.text = bookingList[position].building

        var date1 = bookingList[position].date
        var date2 = ""
        var j = 0
        var i =0
        var n = date1.length
        while(j<n) {
            if (i == 2 || i == 5){
                date2 = "$date2/"
                i++
            }
            else{
                date2 += date1[j]
                j++
                i++
            }
        }
        Log.d("res",date2)


        holder.slot.text = "Slot: " + bookingList[position].slot
        holder.date.text = "Date: " + date2


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewBookingHistoryAdapter.DetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.booking_history_item,parent,false)
        return DetailsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    fun setList(list: MutableList<BookingHistory>){
        bookingList = list
        notifyDataSetChanged()
    }
}

