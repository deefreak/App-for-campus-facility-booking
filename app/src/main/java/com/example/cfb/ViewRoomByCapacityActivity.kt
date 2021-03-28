package com.example.cfb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ViewRoomByCapacityActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var viewRoomByCapacityAdapter: ViewRoomByCapacityAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_room_by_capacity)

        var capacity = intent.extras?.get("capacity") as String


        val text: TextView = findViewById(R.id.text)
        text.text = "All Rooms Of Capacity $capacity"

        val firestore = FirebaseFirestore.getInstance()
        var list: MutableList<ClassRoom> = mutableListOf()

        firestore.collection("ClassRooms").whereEqualTo("Strength",capacity).get()
            .addOnSuccessListener {
                for(document in it){
                    list.add(document.toObject(ClassRoom::class.java))
                }

                var list1: MutableList<ClassRoom> = mutableListOf()

                recyclerView = findViewById(R.id.recyclerCapacity)
                viewRoomByCapacityAdapter = ViewRoomByCapacityAdapter(this,list)


                recyclerView.adapter = viewRoomByCapacityAdapter
                recyclerView.layoutManager = LinearLayoutManager(this)
            }


    }
}