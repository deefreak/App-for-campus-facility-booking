package com.example.cfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*

class RoomSlotActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var roomSlotAdapter: RoomSlotAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_slot)

        var name = intent.extras?.get("name") as String
        var date = intent.extras?.get("date") as String


        var text: TextView = findViewById(R.id.text)
        text.text = "Booking Slots For $name"

        val time = SimpleDateFormat("ddmmyyyyHHmmss").format(Date())


        val firestore = FirebaseFirestore.getInstance()
        var list: MutableMap<String,String> = mutableMapOf()

        firestore.collection(date).document(name).get()
            .addOnSuccessListener {
                if(it.exists()) {
                    var result = it.data

                    if (result != null) {
                        for ((key,value) in result){
                            if(key == "type"){
                                continue
                            }
                            else {
                                list[key] = value.toString()
                            }
                        }

                    }
                    var list1: MutableList<Pair<String,String>> = mutableListOf()


                    recyclerView = findViewById(R.id.recyclerSlots)
                    roomSlotAdapter = RoomSlotAdapter(this,list1,date,name)


                    recyclerView.adapter = roomSlotAdapter
//                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.layoutManager = GridLayoutManager(this, 2)
                    fetchToDoList(list,time)

                }

                else{
                    Toast.makeText(this,"null",Toast.LENGTH_LONG).show()
                }

            }
    }

    private fun fetchToDoList(list: MutableMap<String,String>,time: String) {
        doAsync {
            var list1: MutableList<Pair<String,String>> = mutableListOf()
            val fireStore = FirebaseFirestore.getInstance()




            val currentTime = time.substring(8,10)
            Log.d("curtim",currentTime)

            for ((key,value) in list){
                var slot = ""
                if(key[6] == 'p'){
                    slot = (key.substring(4,6).toInt() + 12).toString()
                }
                else{
                    slot = key.substring(4,6)
                }
                Log.d("slot",slot)
                if(slot.toInt() > currentTime.toInt()) {
                    var pair = Pair(key, value)
                    list1.add(pair)
                }
            }

            (recyclerView.adapter as RoomSlotAdapter).notifyDataSetChanged()

            uiThread {
                roomSlotAdapter.setList(list1)
            }
        }
    }
}