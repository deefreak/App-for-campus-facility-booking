package com.example.cfb

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.editText
import java.util.*

class SearchByClassRoomNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_class_room_name)

        val search: Button = findViewById(R.id.searchButton)

        val datePicker: Button = findViewById(R.id.pickDate)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val pickedDate: TextInputLayout = findViewById(R.id.date)
        var date = ""
        pickedDate.isEnabled = false

        datePicker.setOnClickListener {
            var dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                pickedDate.editText?.setText("" + dayOfMonth + " / " + (monthOfYear.toInt()+1).toString() + " / " + year)
                var monthnumber = (monthOfYear+1).toString()
                var correctMonth = monthOfYear+1
                if(correctMonth < 10){
                    monthnumber = "0$correctMonth"
                }
                var dd =dayOfMonth.toString()
                var correctdd = dayOfMonth
                if(correctdd<10){
                    dd = "0$correctdd"
                }
                date = dd + monthnumber + year.toString()
            }, year, month, day)

            val now = System.currentTimeMillis() - 1000
            //dpd.datePicker.minDate = now
            dpd.datePicker.maxDate = now + (1000*60*60*24*7)
            dpd.show()
        }

        val firestore = FirebaseFirestore.getInstance()
        val name: TextInputLayout = findViewById(R.id.name)
        val building: TextInputLayout = findViewById(R.id.building)

        val spinner1 = findViewById<Spinner>(R.id.spinner1)

        name.error = null
        pickedDate.error = null
        building.error = null
        name.isEnabled = false
        building.isEnabled = false

        var buildings = resources.getStringArray(R.array.Buildings)

        var classList: MutableList<String> = mutableListOf()


        if (spinner1 != null) {
            val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_dropdown_item, buildings)
            spinner1.adapter = adapter

            spinner1.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    building.editText?.setText(buildings[position])
                    name.editText?.setText("")
                    classList.clear()
                    firestore.collection("ClassRooms").whereEqualTo("BuildingName",building.editText?.text.toString()).get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    val doc = document.toObject(ClassRoom::class.java)
                                    classList.add(doc.Name)
                                }
                                val spinner = findViewById<Spinner>(R.id.spinner)

                                val adapter1 = ArrayAdapter(applicationContext,
                                        android.R.layout.simple_spinner_dropdown_item, classList)
                                spinner.adapter = adapter1

                                spinner.onItemSelectedListener = object :
                                        AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(parent: AdapterView<*>,
                                                                view: View, position: Int, id: Long) {
                                        name.editText?.setText(classList[position])
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                        // write code to perform some action
                                    }
                                }

                            }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }









            var list: MutableMap<String, String> = mutableMapOf()
            search.setOnClickListener {
                if (TextUtils.isEmpty(name.editText?.text.toString())) {
                    name.error = "Please Enter a Classroom name"
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty(pickedDate.editText?.text.toString())) {
                    pickedDate.error = "Please Enter a Date for Booking"
                    return@setOnClickListener
                }

                firestore.collection(date).document(name.editText?.text.toString()).get()
                        .addOnSuccessListener {
                            if (it.exists()) {
                                var result = it.data

                                if (result != null) {

                                    val intent = Intent(this, RoomSlotActivity::class.java)


                                    intent.putExtra("date", date)
                                    intent.putExtra("name", name.editText?.text.toString())
                                    startActivity(intent)
                                }
                            } else {
                                Toast.makeText(this, "No Such Room Exists Or Room is Not Available for Booking on this Particular Date", Toast.LENGTH_LONG).show()
                            }

                        }

            }
        }
    }

}