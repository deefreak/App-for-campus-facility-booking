package com.example.cfb

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView


class LabBookingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab_booking)

        val searchByLab: CardView = findViewById(R.id.searchByLabCard)
        val searchByEquipment: CardView = findViewById(R.id.searchByEquipmentCard)
        val goback: ImageView = findViewById(R.id.backB)

        searchByLab.setOnClickListener {
            val intent = Intent(this,SearchByLabNameActivity::class.java)
            startActivity(intent)
        }

        searchByEquipment.setOnClickListener {
            val intent = Intent(this,SearchByLabNameActivity::class.java)
            startActivity(intent)
        }

        goback.setOnClickListener {
            val intent = Intent(this,HomePageActivity::class.java)
            startActivity(intent)
        }

    }
}