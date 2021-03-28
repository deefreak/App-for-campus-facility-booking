package com.example.cfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val searchByRoom: CardView = findViewById(R.id.searchByRoom)
        val searchByStrength: CardView = findViewById(R.id.searchByStrength)
        val viewBooking: CardView = findViewById(R.id.viewBooking)

        val auth = FirebaseAuth.getInstance()
        val logout: CardView = findViewById(R.id.logout)


        searchByRoom.setOnClickListener {
            val intent = Intent(this,SearchByClassRoomNameActivity::class.java)
            startActivity(intent)

        }
        searchByStrength.setOnClickListener {
            val intent = Intent(this,SearchByClassRoomCapacityActivity::class.java)
            startActivity(intent)

        }

        viewBooking.setOnClickListener {
            val intent = Intent(this,ViewBookingHistoryActivity::class.java)
            startActivity(intent)
        }

        logout.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

    }
}