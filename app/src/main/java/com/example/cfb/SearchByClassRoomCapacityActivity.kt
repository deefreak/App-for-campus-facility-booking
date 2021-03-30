package com.example.cfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore

class SearchByClassRoomCapacityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_class_room_capacity)



        val search: Button = findViewById(R.id.searchButton)

        val capacity: TextInputLayout = findViewById(R.id.capacity)

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

                            val intent = Intent(this,ViewRoomByCapacityActivity::class.java)

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