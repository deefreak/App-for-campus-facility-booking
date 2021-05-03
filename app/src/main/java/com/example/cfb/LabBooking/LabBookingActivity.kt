package com.example.cfb.LabBooking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.cfb.HomePageActivity
import com.example.cfb.R


class LabBookingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab_booking)

        val searchByLab: Button = findViewById(R.id.searchByLabNameBox)
        val bookedLabs: Button = findViewById(R.id.bookedLabs)
        val goback: ImageView = findViewById(R.id.backB)

        searchByLab.setOnClickListener {
            val intent = Intent(this, SearchByLabNameActivity::class.java)
            startActivity(intent)
        }

        bookedLabs.setOnClickListener {
            val intent = Intent(this, SearchLabBookingActivity::class.java)
            startActivity(intent)
        }

        goback.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

    }
}