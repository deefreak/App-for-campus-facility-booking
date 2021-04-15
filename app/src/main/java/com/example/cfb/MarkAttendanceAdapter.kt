package com.example.cfb

import android.content.Context
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
import com.example.cfb.models.BookingHistory


import java.text.SimpleDateFormat
import java.util.*


class MarkAttendanceAdapter(var context: Context, var ongoingList: MutableList<BookingHistory>):
    RecyclerView.Adapter<MarkAttendanceAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var slot: TextView = itemView.findViewById(R.id.slot)
        var name: TextView = itemView.findViewById(R.id.name)
        var date: TextView = itemView.findViewById(R.id.date)
        var building: TextView = itemView.findViewById(R.id.buildingname)
        var statusButton: Button = itemView.findViewById(R.id.statusButton)


    }
    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {

        holder.name.text = ongoingList[position].facilityName
        holder.building.text = ongoingList[position].building
        holder.slot.text = "Slot: " + ongoingList[position].slot

        var type = ""
        var date1 = ongoingList[position].date
        var slot = ongoingList[position].slot
        val date2 = date1.substring(6,8) + date1.substring(4,6) + date1.substring(0,4)
        val firestore =  FirebaseFirestore.getInstance().collection(date2)
        firestore.document(ongoingList[position].facilityName).get()
            .addOnSuccessListener {
                type = it.getString("type").toString()

                var currentDate = SimpleDateFormat("dd-MM-yyyy").format(Date())

                holder.name.text = "$type: " + ongoingList[position].facilityName
                holder.date.text = "Date: " + currentDate

            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mark_attendance_card,parent,false)
        return DetailsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return ongoingList.size
    }

    fun setList(list: MutableList<BookingHistory>){
        ongoingList = list

        notifyDataSetChanged()
    }
}

