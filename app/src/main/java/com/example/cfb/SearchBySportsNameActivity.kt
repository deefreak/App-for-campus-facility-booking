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

import java.util.*

class SearchBySportsNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_sports_name)

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
                var monthnumber = ""
                var correctMonth = monthOfYear+1
                if(correctMonth < 10){
                    monthnumber = "0$correctMonth"
                }
                date = dayOfMonth.toString() + monthnumber + year.toString()
            }, year, month, day)

            val now = System.currentTimeMillis() - 1000
            //dpd.datePicker.minDate = now
            dpd.datePicker.maxDate = now + (1000*60*60*24*7)
            dpd.show()
        }




        val name: TextInputLayout = findViewById(R.id.name)

        name.error = null
        pickedDate.error = null
        name.isEnabled = false

        val spinner: Spinner = findViewById(R.id.spinner)



        val firestore = FirebaseFirestore.getInstance()
        val sportNames: MutableList<String> = mutableListOf()
        firestore.collection("Sports").get()
            .addOnSuccessListener {
                for(document in it){
                    val sport = document.toObject(Sport::class.java)
                    sportNames.add(sport.Name)
                }
                val adapter = ArrayAdapter(applicationContext,
                    android.R.layout.simple_spinner_dropdown_item, sportNames)
                spinner.adapter = adapter

                spinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>,
                                                view: View, position: Int, id: Long) {
                        name.editText?.setText(sportNames[position])
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // write code to perform some action
                    }
                }
            }

        var list: MutableMap<String,String> = mutableMapOf()
        search.setOnClickListener {
            if(TextUtils.isEmpty(name.editText?.text.toString())){
                name.error = "Please Enter a Sports name"
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(pickedDate.editText?.text.toString())){
                pickedDate.error = "Please Enter a Date for Booking"
                return@setOnClickListener
            }

            firestore.collection(date).document(name.editText?.text.toString()).get()
                .addOnSuccessListener {
                    if(it.exists()) {
                        var result = it.data

                        if (result != null) {

                            val intent = Intent(this,SportsSlotActivity::class.java)


                            intent.putExtra("date", date)
                            intent.putExtra("name",name.editText?.text.toString())
                            startActivity(intent)
                        }
                    }
                    else{
                        Toast.makeText(this,"No Such Sports Exists Or Sport is Not Available for Booking on this Particular Date",
                            Toast.LENGTH_LONG).show()
                    }

                }

        }
    }
}
