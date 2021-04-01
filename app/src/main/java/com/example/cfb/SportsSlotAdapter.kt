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


class SportsSlotAdapter(var context: Context, var slots: MutableList<Pair<String,String>>,var date: String,var facilityName: String):
    RecyclerView.Adapter<SportsSlotAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var slot: TextView = itemView.findViewById(R.id.slot)

        var bookButton: Button = itemView.findViewById(R.id.bookButton)


    }
    override fun onBindViewHolder(holder: SportsSlotAdapter.DetailsViewHolder, position: Int) {
        var slotText = ""
        var details = slots[position].first

        var originalSlot = slots[position].first
        var startTime = details.substring(0,2)

        var endTime = details.substring(4,6)

        if(startTime[0] == '0'){
            startTime = startTime[1].toString()
        }
        if(endTime[0] == '0'){
            endTime = endTime[1].toString()
        }

        if(details[6] == 'p'){
            slotText = "$startTime:00 PM - $endTime:00 PM"
            holder.slot.text = "Slot: "+ startTime + ":00 PM - "+ endTime + ":00 PM"
        }
        else{
            slotText = "$startTime:00 AM - $endTime:00 AM"
            holder.slot.text = "Slot: "+ startTime + ":00 AM - "+ endTime + ":00 AM"
        }




        var status = slots[position].second
        if (status == "available"){
            holder.bookButton.text = "Book Now"
            var color = Color.GREEN
            holder.bookButton.setBackgroundColor(color)

        }

        else if(status == "acadSlots"){
            holder.bookButton.text = "Academic Slot"
            val color = Color.GRAY
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

                                    val intent = Intent(context,ConfirmBookingActivity::class.java)


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
    ): SportsSlotAdapter.DetailsViewHolder {
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

