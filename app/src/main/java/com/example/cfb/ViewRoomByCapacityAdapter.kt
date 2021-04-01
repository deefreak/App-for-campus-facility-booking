package com.example.cfb

import android.app.Activity
import android.app.DatePickerDialog
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
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import androidx.core.content.ContextCompat.startActivity


import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.find
import org.w3c.dom.Text
import java.util.*


class ViewRoomByCapacityAdapter(var context: Context, var classlist: MutableList<ClassRoom>):
    RecyclerView.Adapter<ViewRoomByCapacityAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var roomName: TextView = itemView.findViewById(R.id.room)

        var viewButton: Button = itemView.findViewById(R.id.viewslots)
        var departmentName: TextView = itemView.findViewById(R.id.department)
        var buildingName: TextView = itemView.findViewById(R.id.building)
        var datePicker: Button = itemView.findViewById(R.id.pickDate)

        var date: TextInputLayout = itemView.findViewById(R.id.date)


    }
    override fun onBindViewHolder(holder: ViewRoomByCapacityAdapter.DetailsViewHolder, position: Int) {

        holder.roomName.text = classlist[position].Name
        holder.departmentName.text = classlist[position].Department
        holder.buildingName.text = classlist[position].BuildingName
        holder.date.isEnabled = false

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var date1 = ""

        holder.datePicker.setOnClickListener {
            var dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                holder.date.editText?.setText("" + dayOfMonth + " / " + (monthOfYear.toInt()+1).toString() + " / " + year)
                var monthnumber = ""
                var correctMonth = monthOfYear+1
                if(correctMonth < 10){
                    monthnumber = "0$correctMonth"
                }
                date1 = dayOfMonth.toString() + monthnumber + year.toString()
            }, year, month, day)

            val now = System.currentTimeMillis() - 1000
            //dpd.datePicker.minDate = now
            dpd.datePicker.maxDate = now + (1000*60*60*24*7)
            dpd.show()
        }


        holder.viewButton.setOnClickListener {
            val dateText = date1
            holder.date.error = null


            if (TextUtils.isEmpty(dateText)) {
                holder.date.error = "Date is required"
                return@setOnClickListener
            }
            val intent = Intent(context,RoomSlotActivity::class.java)
            intent.putExtra("name",holder.roomName.text as String)
            intent.putExtra("date",dateText)

            val firestore = FirebaseFirestore.getInstance()
            firestore.collection(dateText).document(holder.roomName.text as String).get()
                .addOnSuccessListener{
                    if(it.exists()){
                        val intent = Intent(context,RoomSlotActivity::class.java)
                        intent.putExtra("name",holder.roomName.text as String)
                        intent.putExtra("date",dateText)
                        context.startActivity(intent)
                    }
                    else{
                        Toast.makeText(context,"No Slots On this Date. Try Another Date(Pick Date from the Pick Date Button Only).",Toast.LENGTH_LONG).show()

                    }
                }




        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewRoomByCapacityAdapter.DetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.room_capacity,parent,false)
        return DetailsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return classlist.size
    }

    fun setList(list: MutableList<ClassRoom>){
        classlist = list
        notifyDataSetChanged()
    }
}

