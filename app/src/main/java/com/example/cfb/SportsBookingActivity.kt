package com.example.cfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.cardview.widget.CardView

class SportsBookingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sports_booking)

        val searchBySports: CardView = findViewById(R.id.searchBySportsCard)
        val goback: ImageView = findViewById(R.id.backB)

        searchBySports.setOnClickListener {
            val intent = Intent(this,SearchBySportsNameActivity::class.java)
            startActivity(intent)
        }

        goback.setOnClickListener {
            val intent = Intent(this,HomePageActivity::class.java)
            startActivity(intent)
        }
    }
}