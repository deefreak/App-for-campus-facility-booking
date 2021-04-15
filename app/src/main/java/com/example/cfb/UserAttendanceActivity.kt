package com.example.cfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cfb.Adapters.UserAttendanceAdapter
import com.example.cfb.Adapters.ViewBookingHistoryAdapter
import com.example.cfb.models.BookingHistory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class UserAttendanceActivity: AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var userAttendanceAdapter: UserAttendanceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_attendance)

        val fireStore = FirebaseFirestore.getInstance()

        var list: MutableList<BookingHistory> = mutableListOf()

        val goback: ImageView = findViewById(R.id.backB)
        goback.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }


        recyclerView = findViewById(R.id.recyclerHistory)
        userAttendanceAdapter = UserAttendanceAdapter(this, list)


        recyclerView.adapter = userAttendanceAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchToDoList()

    }

    private fun fetchToDoList() {
        doAsync {
            var list: MutableList<BookingHistory> = mutableListOf()
            val fireStore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val email = auth.currentUser.email
            auth.currentUser?.uid?.let {
                fireStore.collection("Users").document(it).collection("AttendanceRecord")

                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            list.add(document.toObject(BookingHistory::class.java))

                        }
                        for (i in list){
                            var date = i.date
                            var date1 = date.substring(4,8) + date.substring(2,4) + date.substring(0,2)
                            i.date = date1
                        }
                        list.sortByDescending {it.date}
                        (recyclerView.adapter as UserAttendanceAdapter).notifyDataSetChanged()
                    }
                    .addOnFailureListener {
                        Log.e("No","Error")
                    }
            }

            uiThread {

                userAttendanceAdapter.setList(list)
            }
        }
    }
}