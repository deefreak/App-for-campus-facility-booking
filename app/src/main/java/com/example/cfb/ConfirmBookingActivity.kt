package com.example.cfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.cfb.models.BookingHistory
import com.example.cfb.models.ClassRoom
import com.example.cfb.models.Sport
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ConfirmBookingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_booking)

        val name = intent.extras?.get("name") as String
        val date = intent.extras?.get("date") as String
        val email = intent.extras?.get("email") as String
        val slot = intent.extras?.get("slot") as String
        val originalSlot = intent.extras?.get("originalSlot") as String
        val facilityName = intent.extras?.get("facilityName") as String

        var nameText: TextInputLayout = findViewById(R.id.name)
        var dateText: TextInputLayout = findViewById(R.id.date)
        var emailText: TextInputLayout = findViewById(R.id.email)
        var slotText: TextInputLayout = findViewById(R.id.slot)
        val facilityNameText: TextInputLayout = findViewById(R.id.facilityName)

        val purpose: TextInputLayout = findViewById(R.id.purpose)
        var confirmButton: Button = findViewById(R.id.confirmButton)

        var date2 = ""
        var j = 0
        var i =0
        var n = date.length
        while(j<n) {
            if (i == 2 || i == 5){
                date2 = "$date2/"
                i++
            }
            else{
                date2 += date[j]
                j++
                i++
            }
        }

        nameText.editText?.setText(name)
        dateText.editText?.setText(date2)
        emailText.editText?.setText(email)
        slotText.editText?.setText(slot)
        facilityNameText.editText?.setText(facilityName)

        nameText.isEnabled = false
        dateText.isEnabled = false
        emailText.isEnabled = false
        slotText.isEnabled = false
        facilityNameText.isEnabled = false

        confirmButton.setOnClickListener {
            val firestore = FirebaseFirestore.getInstance()
            val list: MutableList<ClassRoom> = mutableListOf()
            val list1: MutableList<Sport> = mutableListOf()
            var type=""
            firestore.collection(date).document(facilityName).get()
                .addOnSuccessListener {
                    type = it.getString("type").toString()
                    firestore.collection(type).whereEqualTo("Name", facilityName).get()
                        .addOnSuccessListener {
                            for (document in it) {
                                if(type == "ClassRooms") {
                                    list.add(document.toObject(ClassRoom::class.java))
                                }
                                else if(type == "Sports"){
                                    list1.add(document.toObject(Sport::class.java))
                                }
                            }
                            var buildingName = ""
                            if(type == "ClassRooms") {
                                buildingName = list[0].BuildingName
                            }
                            val purposeText = purpose.editText?.text.toString()
                            val id = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
                            val bookingHistory = BookingHistory(
                                    email,
                                    date,
                                    slot,
                                    name,
                                    facilityName,
                                    purposeText,
                                    buildingName,
                                    type,
                                    id
                            )

                            firestore.collection("BookingHistory").document(id).set(bookingHistory)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        firestore.collection(date).document(facilityName)
                                            .update(originalSlot, "booked")
                                        Toast.makeText(
                                            this,
                                            "SuccessFully Booked",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        val intent = Intent(this, HomePageActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                }


        }



    }
}