package com.example.cfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val searchByRoom: CardView = findViewById(R.id.searchByRoom)

        searchByRoom.setOnClickListener {
            val intent = Intent(this,SearchByClassRoomNameActivity::class.java)
            startActivity(intent)

        }

    }
}