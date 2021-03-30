package com.example.cfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val classBooking: CardView = findViewById(R.id.classbook)
        val labBooking: CardView = findViewById(R.id.labbook)
        val sportBooking: CardView = findViewById(R.id.sportbook)
        val viewBooking: CardView = findViewById(R.id.viewBooking)

        val auth = FirebaseAuth.getInstance()
        val logout: ImageView = findViewById(R.id.logout)


        classBooking.setOnClickListener {
            val intent = Intent(this,ClassRoomBookingActivity::class.java)
            startActivity(intent)
        }

        labBooking.setOnClickListener {
            val intent = Intent(this,LabBookingActivity::class.java)
            startActivity(intent)
        }

        sportBooking.setOnClickListener {
            val intent = Intent(this,ClassRoomBookingActivity::class.java)
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