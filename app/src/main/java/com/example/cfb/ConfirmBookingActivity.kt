package com.example.cfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import org.jetbrains.anko.find

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

        nameText.editText?.setText(name)
        dateText.editText?.setText(date)
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
            firestore.collection("ClassRooms").whereEqualTo("Name",facilityName).get()
                .addOnSuccessListener {
                    for(document in it){
                        list.add(document.toObject(ClassRoom::class.java))

                    }
                    var buildingName = list[0].BuildingName
                    val purposeText = purpose.editText?.text.toString()
                    val bookingHistory = BookingHistory(email,date,slot,name,facilityName,purposeText,buildingName)

                    firestore.collection("BookingHistory").document().set(bookingHistory)
                        .addOnCompleteListener {task->
                            if(task.isSuccessful){
                                firestore.collection(date).document(facilityName).update(originalSlot, "booked")
                                Toast.makeText(this,"SuccessFully Booked",Toast.LENGTH_LONG).show()
                                val intent = Intent(this,HomePageActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
                            }
                        }
                }


        }



    }
}