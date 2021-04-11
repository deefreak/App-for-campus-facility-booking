package com.example.cfb.ClassroomBooking

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.cfb.HomePageActivity
import com.example.cfb.R


class ClassRoomBookingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_room_booking)

        val searchByRoom: CardView = findViewById(R.id.searchByRoomCard)
        val searchByStrength: CardView = findViewById(R.id.searchByCapacityCard)
        val goback: ImageView = findViewById(R.id.backB)

        searchByRoom.setOnClickListener {
            val intent = Intent(this, SearchByClassRoomNameActivity::class.java)
            startActivity(intent)
        }

        searchByStrength.setOnClickListener {
            val intent = Intent(this, SearchByClassRoomCapacityActivity::class.java)
            startActivity(intent)
        }

        goback.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

    }
}