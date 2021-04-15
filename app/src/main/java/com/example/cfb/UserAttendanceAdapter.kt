package com.example.cfb.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

import android.util.Log
import com.example.cfb.R
import com.example.cfb.ViewAllAttendanceActivity
import com.example.cfb.models.BookingHistory
import org.jetbrains.anko.find


import java.text.SimpleDateFormat
import java.util.*


class UserAttendanceAdapter(var context: Context, var bookingList: MutableList<BookingHistory>):
    RecyclerView.Adapter<UserAttendanceAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var slot: TextView = itemView.findViewById(R.id.slot)
        var name: TextView = itemView.findViewById(R.id.name)
        var date: TextView = itemView.findViewById(R.id.date)
        var building: TextView = itemView.findViewById(R.id.buildingname)

    }
    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {

        holder.name.text = bookingList[position].facilityName
        holder.building.text = bookingList[position].building
        holder.slot.text = "Slot: " + bookingList[position].slot
        val id = bookingList[position].id

        var type = ""
        var date1 = bookingList[position].date
        var slot = bookingList[position].slot


        var date2 = ""
        var j = 0
        var i =0
        var n = date1.length
        while(j<n) {
            if (i == 4 || i == 7){
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


        holder.name.text = bookingList[position].type + ": " + bookingList[position].facilityName
        holder.date.text = "Date: " + date2

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_attendance_item,parent,false)
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

