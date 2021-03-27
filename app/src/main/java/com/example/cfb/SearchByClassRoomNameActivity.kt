package com.example.cfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class SearchByClassRoomNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_class_room_name)

        val search: Button = findViewById(R.id.searchButton)

        val date: TextInputLayout = findViewById(R.id.date)
        val name: TextInputLayout = findViewById(R.id.name)

        val firestore = FirebaseFirestore.getInstance()
        var list: MutableMap<String,String> = mutableMapOf()
        search.setOnClickListener {
            val dateText = date.editText?.text.toString()
            firestore.collection(dateText).document(name.editText?.text.toString()).get()
                .addOnSuccessListener {
                    if(it.exists()) {
                        var result = it.data

                        if (result != null) {

                            val intent = Intent(this,RoomSlotActivity::class.java)


                            intent.putExtra("date",dateText)
                            intent.putExtra("name",name.editText?.text.toString())
                            startActivity(intent)
                        }
                    }
                    else{
                        Toast.makeText(this,"null",Toast.LENGTH_LONG).show()
                    }

                }

        }
    }
}