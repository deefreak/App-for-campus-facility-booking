package com.example.cfb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cfb.Adapters.ViewBookingHistoryAdapter
import com.example.cfb.models.BookingHistory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ViewBookingHistoryActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var viewBookingHistoryAdapter: ViewBookingHistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_booking_history)

        val fireStore = FirebaseFirestore.getInstance()

        var list: MutableList<BookingHistory> = mutableListOf()


        recyclerView = findViewById(R.id.recyclerHistory)
        viewBookingHistoryAdapter = ViewBookingHistoryAdapter(this, list)


        recyclerView.adapter = viewBookingHistoryAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchToDoList()

    }

    private fun fetchToDoList() {
        doAsync {
            var list: MutableList<BookingHistory> = mutableListOf()
            val fireStore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val email = auth.currentUser.email
            fireStore.collection("BookingHistory").whereEqualTo("bookedBy",email)

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
                    (recyclerView.adapter as ViewBookingHistoryAdapter).notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Log.e("No","Error")
                }

            uiThread {

                viewBookingHistoryAdapter.setList(list)
            }
        }
    }
}