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
import android.widget.Toast
import com.example.cfb.R
import com.example.cfb.models.BookingHistory
import com.example.cfb.models.attendance
import com.google.firebase.auth.FirebaseAuth


import java.text.SimpleDateFormat
import java.util.*


class MarkAttendanceAdapter(var context: Context, var ongoingList: MutableList<BookingHistory>):
    RecyclerView.Adapter<MarkAttendanceAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var slot: TextView = itemView.findViewById(R.id.slot)
        var name: TextView = itemView.findViewById(R.id.name)
        var date: TextView = itemView.findViewById(R.id.date)
        var building: TextView = itemView.findViewById(R.id.buildingname)
        var markButton: Button = itemView.findViewById(R.id.markButton)


    }
    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {

        holder.name.text = ongoingList[position].facilityName
        holder.building.text = ongoingList[position].building
        holder.slot.text = "Slot: " + ongoingList[position].slot

        var type = ""
        var date1 = ongoingList[position].date
        var slot = ongoingList[position].slot


                var currentDate = SimpleDateFormat("dd-MM-yyyy").format(Date())

                holder.name.text = ongoingList[position].type + ": " + ongoingList[position].facilityName
                holder.date.text = "Date: " + currentDate

        val id = ongoingList[position].id
        holder.markButton.setOnClickListener {
            val firestore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val email = auth.currentUser?.email
            if (email != null) {
                val data = attendance(email)
                firestore.collection("BookingHistory").document(id).collection("Attendees").document().set(data)
                        .addOnSuccessListener {
                            Toast.makeText(context,"MArked",Toast.LENGTH_LONG).show()

                            holder.markButton.text = "Marked"
                            holder.markButton.setBackgroundColor(Color.GREEN)
                            holder.markButton.isEnabled = false
                            auth.currentUser?.uid?.let { it1 -> firestore.collection("Users").document(it1)
                                    .collection("AttendanceRecord").document().set(ongoingList[position])}
                        }
            }
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

