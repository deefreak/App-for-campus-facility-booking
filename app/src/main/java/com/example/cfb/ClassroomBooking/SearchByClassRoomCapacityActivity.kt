package com.example.cfb.ClassroomBooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.cfb.HomePageActivity
import com.example.cfb.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore

class SearchByClassRoomCapacityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_class_room_capacity)

        val search: Button = findViewById(R.id.searchButton)
        val goback: ImageView = findViewById(R.id.backB)

        goback.setOnClickListener {
            val intent = Intent(this, ClassRoomBookingActivity::class.java)
            startActivity(intent)
        }

        val capacity: TextInputLayout = findViewById(R.id.capacity)

        val spinner = findViewById<Spinner>(R.id.spinner1)
        capacity.isEnabled = false

        val strength = resources.getStringArray(R.array.Capacity)
        val adapter1 = ArrayAdapter(applicationContext,
                android.R.layout.simple_spinner_dropdown_item, strength)
        spinner.adapter = adapter1

        spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                capacity.editText?.setText(strength[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val firestore = FirebaseFirestore.getInstance()
        var c = 0
        search.setOnClickListener {
            val capacityText = capacity.editText?.text.toString()
            firestore.collection("ClassRooms").whereEqualTo("Strength",capacity.editText?.text.toString()).get()
                .addOnSuccessListener {documents->
                    for(document in documents) {
                        c = 1
                    }
                    if(c == 1){

                            val intent = Intent(this, ViewRoomByCapacityActivity::class.java)

                            intent.putExtra("capacity",capacityText)
                            startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"No Room with this Strength Exists", Toast.LENGTH_LONG).show()
                    }

                }

        }
    }


}