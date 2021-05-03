package com.example.cfb.SportBooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.cfb.HomePageActivity
import com.example.cfb.R

class SportsBookingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sports_booking)

        val searchBySports: Button = findViewById(R.id.searchBySportsNameBox)
        val bookedSports: Button = findViewById(R.id.bookedSportComplexes)
        val goback: ImageView = findViewById(R.id.backB)

        searchBySports.setOnClickListener {
            val intent = Intent(this, SearchBySportsNameActivity::class.java)
            startActivity(intent)
        }

        bookedSports.setOnClickListener {
            val intent = Intent(this, SearchSportsBookingActivity::class.java)
            startActivity(intent)
        }

        goback.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }
    }
}