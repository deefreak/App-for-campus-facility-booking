package com.example.cfb

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cfb.MarkAttendanceAdapter
import com.example.cfb.models.BookingHistory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*

class MarkAttendanceActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var markAttendanceAdapter: MarkAttendanceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark_attendance)

        val fireStore = FirebaseFirestore.getInstance()

        var list: MutableList<BookingHistory> = mutableListOf()

        val goback: ImageView = findViewById(R.id.backB)
        goback.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }


        recyclerView = findViewById(R.id.recyclerHistory)
        markAttendanceAdapter = MarkAttendanceAdapter(this, list)


        recyclerView.adapter = markAttendanceAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchToDoList()

    }

    private fun fetchToDoList() {
        doAsync {
            var list: MutableList<BookingHistory> = mutableListOf()
            var ongoinglist: MutableList<BookingHistory> = mutableListOf()
            val fireStore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val email = auth.currentUser.email
            var currentDate = SimpleDateFormat("ddMMyyyy").format(Date())

            fireStore.collection("BookingHistory").whereEqualTo("date",currentDate)

                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            list.add(document.toObject(BookingHistory::class.java))

                        }
                        for (i in list){
                            var slot = i.slot
                            var time = SimpleDateFormat("HHmmss").format(Date())
                            var currenthour = time.substring(0,2)
                            var startTime = ""
                            var endTime = ""

                            if(slot[5] == 'A'){
                                startTime = slot.substring(0,2)
                            }
                            else{
                                startTime = (slot.substring(0,2).toInt() + 12).toString()
                            }
                            if(startTime == "24"){
                                startTime = "12"
                            }
                            if(slot[15] == 'A'){
                                endTime = slot.substring(10,12)
                            }
                            else{
                                endTime = (slot.substring(10,12).toInt() + 12).toString()
                            }
                            if(endTime == "24"){
                                endTime = "12"
                            }

                            if(currenthour.toInt() < startTime.toInt()) {
                            }
                            else if(currenthour.toInt() >= startTime.toInt() && currenthour.toInt() < endTime.toInt()){
                                val id = i.id
                                val auth = FirebaseAuth.getInstance()
                                var c = 0
                                fireStore.collection("BookingHistory").document(id).collection("Attendees").whereEqualTo("email",auth.currentUser?.email).get()
                                        .addOnSuccessListener {
                                            if(it.isEmpty){
                                                ongoinglist.add(i)
                                            }
                                            (recyclerView.adapter as MarkAttendanceAdapter).notifyDataSetChanged()
                                        }
                            }
                        }
//                        ongoinglist.sortByDescending {it.date}

                    }
                    .addOnFailureListener {
                        Log.e("No","Error")
                    }

            uiThread {

                markAttendanceAdapter.setList(ongoinglist)
            }
        }
    }
}