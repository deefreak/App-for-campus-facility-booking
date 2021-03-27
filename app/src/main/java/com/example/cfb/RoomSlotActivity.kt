package com.example.cfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class RoomSlotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_slot)

        var name = intent.extras?.get("name") as String
        var date = intent.extras?.get("date") as String

        val firestore = FirebaseFirestore.getInstance()
        var list: MutableMap<String,String> = mutableMapOf()

        firestore.collection(date).document(name).get()
            .addOnSuccessListener {
                if(it.exists()) {
                    var result = it.data

                    if (result != null) {
                        for ((key,value) in result){
                            list[key] = value.toString()
                        }

                    }
                    for ((key,value) in list){
                        Toast.makeText(this,key,Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(this,"null",Toast.LENGTH_LONG).show()
                }

            }
    }
}