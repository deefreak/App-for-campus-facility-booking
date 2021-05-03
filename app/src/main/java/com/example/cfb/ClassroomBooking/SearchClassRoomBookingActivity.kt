package com.example.cfb.ClassroomBooking

import android.R.attr.button
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.contains
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cfb.Adapters.ViewBookingHistoryAdapter
import com.example.cfb.R
import com.example.cfb.models.BookingHistory
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class SearchClassRoomBookingActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var viewBookingHistoryAdapter: ViewBookingHistoryAdapter
    var list: MutableList<BookingHistory> = mutableListOf()
    var list1: MutableList<BookingHistory> = mutableListOf()
    lateinit var search: Button
    lateinit var pickedDate: Button
    lateinit var nameT: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_class_room_booking)
        val auth = FirebaseAuth.getInstance()

        search = findViewById(R.id.searchButton)
        val searchmenu: ImageButton = findViewById(R.id.sortButton)
        nameT = findViewById(R.id.name)
        nameT.hint = "Select from Menu"
        nameT.isEnabled = false
        pickedDate = findViewById(R.id.pickDate)


        searchmenu.setOnClickListener{
            showPopup(searchmenu)
        }

        /*searchButton.setOnClickListener {
            val nameText = name.editText?.text.toString()
            name.error = ""
            if(TextUtils.isEmpty(nameText)){
                name.error = "Name is required"
                return@setOnClickListener
            }
            //val intent = Intent(this,ViewByNameActivity::class.java)
            //intent.putExtra("name",name.editText?.text.toString())
            //startActivity(intent)
        }*/



        recyclerView = findViewById(R.id.recyclerView)
        viewBookingHistoryAdapter = ViewBookingHistoryAdapter(this,list1)


        recyclerView.adapter = viewBookingHistoryAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchToDoList()

    }
    private fun fetchToDoList() {
        doAsync {
            val fireStore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val email = auth.currentUser.email
            fireStore.collection("BookingHistory").whereEqualTo("bookedBy",auth.currentUser.email).whereEqualTo("type","ClassRooms").get()

                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            list.add(document.toObject(BookingHistory::class.java))
                            list1.add(document.toObject(BookingHistory::class.java))
                        }
                        for (i in list){
                            var date = i.date
                            var date1 = date.substring(4,8) + date.substring(2,4) + date.substring(0,2)
                            i.date = date1
                        }
                        for (i in list1){
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
        popup.inflate(R.menu.search_classroom)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.header1 -> {

                    nameT.hint = "Enter Name"
                    pickedDate.visibility = View.GONE
                    nameT.editText?.setText("")
                    nameT.isEnabled = true
                    search.setOnClickListener {
                        list1.clear()
                        viewBookingHistoryAdapter = ViewBookingHistoryAdapter(this, list1)


                        recyclerView.adapter = viewBookingHistoryAdapter

                        recyclerView.layoutManager = LinearLayoutManager(this)
                        var nameentered = nameT.editText?.text.toString()
                        for(item in list){
                            if(item.facilityName.startsWith(nameentered)){
                                list1.add(item)
                            }
                        }
                        (recyclerView.adapter as ViewBookingHistoryAdapter).notifyDataSetChanged()
                    }
                    //val intent = Intent(this,AssignedPatients::class.java)
                    //startActivity(intent)
                }

                R.id.header2 -> {
                    nameT.hint = "Enter Date"
                    pickedDate.visibility = View.VISIBLE
                    nameT.isEnabled = false
                    nameT.editText?.setText("")
                    val c = Calendar.getInstance()
                    val year = c.get(Calendar.YEAR)
                    val month = c.get(Calendar.MONTH)
                    val day = c.get(Calendar.DAY_OF_MONTH)


                    var date = ""


                    pickedDate.setOnClickListener {
                        var dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            // Display Selected date in TextView
                            nameT.editText?.setText("" + dayOfMonth + " / " + (monthOfYear.toInt()+1).toString() + " / " + year)
                            var daynumber = ""
                            var monthnumber = (monthOfYear+1).toString()
                            var correctMonth = monthOfYear + 1
                            if (correctMonth < 10) {
                                monthnumber = "0$correctMonth"
                            }
                            if (dayOfMonth < 10) {
                                daynumber = "0$dayOfMonth"
                                date = year.toString() + monthnumber + daynumber
                            }
                            else {date = year.toString() + monthnumber + dayOfMonth.toString()}
                        }, year, month, day)

                        val now = System.currentTimeMillis() - 1000
                        //dpd.datePicker.minDate = now
                        dpd.datePicker.maxDate = now + (1000*60*60*24*7)
                        dpd.show()
                    }
                    search.setOnClickListener {
                        list1.clear()
                        viewBookingHistoryAdapter = ViewBookingHistoryAdapter(this, list1)


                        recyclerView.adapter = viewBookingHistoryAdapter

                        recyclerView.layoutManager = LinearLayoutManager(this)
                        for(item in list){
                            if(item.date == date){
                                list1.add(item)
                            }
                        }
                        (recyclerView.adapter as ViewBookingHistoryAdapter).notifyDataSetChanged()
                    }
                    //val intent = Intent(this,AssignedPatientsByDate::class.java)
                    //startActivity(intent)
                }
                R.id.header3 -> {
                    nameT.isEnabled = false
                    nameT.hint = "Select from Menu to Search"
                    pickedDate.visibility = View.GONE
                        list1.clear()
                    viewBookingHistoryAdapter = ViewBookingHistoryAdapter(this, list1)


                    recyclerView.adapter = viewBookingHistoryAdapter

                    recyclerView.layoutManager = LinearLayoutManager(this)
                        for(item in list){

                                list1.add(item)
                        }
                        (recyclerView.adapter as ViewBookingHistoryAdapter).notifyDataSetChanged()

                    //val intent = Intent(this,AssignedPatientsByDate::class.java)
                    //startActivity(intent)
                }

            }

            true
        })

        popup.show()
    }

}