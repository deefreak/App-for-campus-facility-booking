package com.example.cfb.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

import com.example.cfb.LabBooking.ConfirmLabBookingActivity
import com.example.cfb.R


import com.google.firebase.auth.FirebaseAuth


class LabSlotAdapter(var context: Context, var slots: MutableList<Pair<String,String>>,var date: String,var facilityName: String):
    RecyclerView.Adapter<LabSlotAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var slot: TextView = itemView.findViewById(R.id.slot)

        var bookButton: Button = itemView.findViewById(R.id.bookButton)


    }
    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        var slotText = ""
        var details = slots[position].first

        var originalSlot = slots[position].first
        var startTime = details.substring(0,2)

        var endTime = details.substring(4,6)

        if(startTime.toInt() > 12){
            startTime = (startTime.toInt()-12).toString()
            if(startTime.length == 1){
                startTime = "0$startTime"
            }
            startTime += ":00PM"
        }
        else if(startTime.toInt() == 12){
            startTime += ":00PM"
        }
        else{
            startTime += ":00AM"
        }
        if(endTime.toInt() > 12){
            endTime = (endTime.toInt()-12).toString()
            if(endTime.length == 1){
                endTime = "0$endTime"
            }
            endTime += ":00PM"
        }
        else if(endTime.toInt() == 12){
            endTime += ":00PM"
        }
        else{
            endTime += ":00AM"
        }

        holder.slot.text = startTime + " - " + endTime
        slotText = startTime + " - " + endTime



        var status = slots[position].second
        if (status == "available"){
            holder.bookButton.text = "Book Now"
            var color = Color.parseColor("#13d162")
            holder.bookButton.setBackgroundColor(color)

        }

        else if(status == "acadSlots"){
            holder.bookButton.text = "Academic Slot"
            val color = Color.parseColor("#8585ad")
            holder.bookButton.setBackgroundColor(color)
        }

        holder.bookButton.setOnClickListener {
            if(holder.bookButton.text == "Already Booked"){
                Toast.makeText(context,"Please Choose Another Slot. This has already been booked",Toast.LENGTH_LONG).show()

            }
            else if(holder.bookButton.text == "Academic Slot"){
                Toast.makeText(context,"Please Choose Another Slot. This is reserved for academic purposes",Toast.LENGTH_LONG).show()
            }
            else{
                val auth = FirebaseAuth.getInstance()
                val id = auth.currentUser?.uid

                val firestore = FirebaseFirestore.getInstance().collection("Users")

                if (id != null) {
                    firestore.document(id).get()
                        .addOnSuccessListener {
                            if(it.exists()) {
                                var result = it.data

                                if (result != null) {

                                    val intent = Intent(context, ConfirmLabBookingActivity::class.java)


                                    intent.putExtra("name",result["name"].toString())
                                    intent.putExtra("email",result["email"].toString())
                                    intent.putExtra("date",date)
                                    intent.putExtra("slot",slotText)
                                    intent.putExtra("facilityName",facilityName)
                                    intent.putExtra("originalSlot",originalSlot)
                                    context.startActivity(intent)
                                }
                            }
                            else{
                                Toast.makeText(context,"null",Toast.LENGTH_LONG).show()
                            }


                        }
                }

            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.slot_item,parent,false)
        return DetailsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return slots.size
    }

    fun setList(list: MutableList<Pair<String,String>>){
        slots = list
        notifyDataSetChanged()
    }
}

