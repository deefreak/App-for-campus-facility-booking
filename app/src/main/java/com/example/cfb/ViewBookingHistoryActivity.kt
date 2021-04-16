package com.example.cfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
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
    var list: MutableList<BookingHistory> = mutableListOf()
    var list1: MutableList<BookingHistory> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_booking_history)

        val fireStore = FirebaseFirestore.getInstance()



        val goback: ImageView = findViewById(R.id.backB)
        val filterButton: ImageView = findViewById(R.id.filterButton)

        filterButton.setOnClickListener{
            showPopup(filterButton)
        }
        goback.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }


        recyclerView = findViewById(R.id.recyclerHistory)
        viewBookingHistoryAdapter = ViewBookingHistoryAdapter(this, list)


        recyclerView.adapter = viewBookingHistoryAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchToDoList()

    }

    private fun fetchToDoList() {
        doAsync {
            val fireStore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val email = auth.currentUser.email
            fireStore.collection("BookingHistory").whereEqualTo("bookedBy",email)

                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        list.add(document.toObject(BookingHistory::class.java))
                        list1.add(document.toObject(BookingHistory::class.java))
                    }
                    for (i in list1){
                        var date = i.date
                        var date1 = date.substring(4,8) + date.substring(2,4) + date.substring(0,2)
                        i.date = date1
                    }
                    for (i in list){
                        var date = i.date
                        var date1 = date.substring(4,8) + date.substring(2,4) + date.substring(0,2)
                        i.date = date1
                    }
                    list1.sortByDescending { it.date }
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

    private fun showPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.search_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.header1 -> {
                    list.clear()
                    viewBookingHistoryAdapter = ViewBookingHistoryAdapter(this, list)


                    recyclerView.adapter = viewBookingHistoryAdapter

                    recyclerView.layoutManager = LinearLayoutManager(this)
                    for(item in list1){
                        list.add(item)
                    }
                    (recyclerView.adapter as ViewBookingHistoryAdapter).notifyDataSetChanged()
                }
                R.id.header2 -> {
                    list.clear()
                    viewBookingHistoryAdapter = ViewBookingHistoryAdapter(this, list)


                    recyclerView.adapter = viewBookingHistoryAdapter

                    recyclerView.layoutManager = LinearLayoutManager(this)
                    for(item in list1){
                        if(item.type == "ClassRooms"){
                            list.add(item)
                        }
                    }
                    (recyclerView.adapter as ViewBookingHistoryAdapter).notifyDataSetChanged()

                }
                R.id.header3 -> {
                    list.clear()
                    viewBookingHistoryAdapter = ViewBookingHistoryAdapter(this, list)


                    recyclerView.adapter = viewBookingHistoryAdapter

                    recyclerView.layoutManager = LinearLayoutManager(this)
                    for(item in list1){
                        if(item.type == "Labs"){
                            list.add(item)
                        }
                    }
                    (recyclerView.adapter as ViewBookingHistoryAdapter).notifyDataSetChanged()
                }
                R.id.header4 -> {
                    list.clear()
                    viewBookingHistoryAdapter = ViewBookingHistoryAdapter(this, list)


                    recyclerView.adapter = viewBookingHistoryAdapter

                    recyclerView.layoutManager = LinearLayoutManager(this)
                    for(item in list1){
                        if(item.type == "Sports"){
                            list.add(item)
                        }
                    }
                    (recyclerView.adapter as ViewBookingHistoryAdapter).notifyDataSetChanged()
                }
            }

            true
        })

        popup.show()
    }
}