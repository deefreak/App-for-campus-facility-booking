package com.example.cfb.AttendanceModule

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cfb.HomePageActivity
import com.example.cfb.Adapters.MarkAttendanceAdapter
import com.example.cfb.R
import com.example.cfb.models.BookingHistory
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Math.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow

class MarkAttendanceActivity : AppCompatActivity() {

    companion object{
        private val REQUEST_PERMISSION_REQUEST_CODE = 2020
    }

    class Run {
        companion object {
            fun after(delay: Long, process: () -> Unit) {
                Handler().postDelayed({
                    process()
                }, delay)
            }
        }
    }

    lateinit var recyclerView: RecyclerView
    lateinit var markAttendanceAdapter: MarkAttendanceAdapter

    var userLatitude = ""
    var userLongitude = ""
    var facilityLatitude = 0.00
    var facilityLongitude = 0.00
    lateinit var Lat : TextView
    lateinit var Lon : TextView
    lateinit var addr : TextView

    /*
    // Center coor of M1 Classroom
    var m1Latitude = 23.8365571
    var m1Longitude = 80.3879593

    var meters = 10
    // number of km per degree = ~111km (111.32 in google maps, but range varies between 110.567km at the equator and 111.699km at the poles)
    // 1km in degree = 1 / 111.32km = 0.0089
    // 1m in degree = 0.0089 / 1000 = 0.0000089
    var coef = meters * 0.0000089
    var new_lat_x1 = m1Latitude - coef
    var new_lat_x2 = m1Latitude + coef
    // pi / 180 = 0.018
    var new_long_y1 = m1Longitude - coef / cos(m1Longitude.toDouble()*0.018)
    var new_long_y2 = m1Longitude + coef / cos(m1Longitude.toDouble()*0.018)
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark_attendance)

        Lat  = findViewById(R.id.tvLatitude)
        Lon  = findViewById(R.id.tvLongitude)
        addr = findViewById(R.id.tvAddress)

        //check permission
        if (ContextCompat.checkSelfPermission(
                applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
                ,
                REQUEST_PERMISSION_REQUEST_CODE
            )
        } else {
            Lat.text = ""
            Lon.text = ""
            addr.text = ""
            userLatitude = ""
            Toast.makeText(this,"Fetching Location...",Toast.LENGTH_LONG).show()
            getCurrentLocation()
        }

        val fireStore = FirebaseFirestore.getInstance()

        var list: MutableList<BookingHistory> = mutableListOf()

        val goback: ImageView = findViewById(R.id.backB)
        goback.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }


        recyclerView = findViewById(R.id.recyclerHistory)
        markAttendanceAdapter =
            MarkAttendanceAdapter(this, list)


        recyclerView.adapter = markAttendanceAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        Run.after(
            2000
        ) {
            fetchToDoList()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_REQUEST_CODE && grantResults.size > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation()
            }else{
                Toast.makeText(this,"Permission Denied!",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getCurrentLocation() {

        var locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        //now getting address from latitude and longitude

        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses:List<Address>

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest,object : LocationCallback(){
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(this@MarkAttendanceActivity)
                        .removeLocationUpdates(this)
                    if (locationResult != null && locationResult.locations.size > 0){
                        var locIndex = locationResult.locations.size-1

                        var latitude = locationResult.locations.get(locIndex).latitude
                        var longitude = locationResult.locations.get(locIndex).longitude
                        Lat.text = "Latitude: "+latitude
                        userLatitude = latitude.toString()
                        Lon.text = "Longitude: "+longitude
                        userLongitude = longitude.toString()

                        addresses = geocoder.getFromLocation(latitude,longitude,1)

                        var address:String = addresses[0].getAddressLine(0)
                        addr.text = address
                    }
                }
            }, Looper.getMainLooper())

    }

    private fun fetchToDoList() {
        doAsync {
            var list: MutableList<BookingHistory> = mutableListOf()
            var ongoinglist: MutableList<BookingHistory> = mutableListOf()
            var ongoingllist: MutableList<BookingHistory> = mutableListOf()
            val fireStore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val email = auth.currentUser.email
            var currentDate = SimpleDateFormat("ddMMyyyy").format(Date())

            fireStore.collection("BookingHistory").whereEqualTo("date",currentDate)

                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            list.add(document.toObject(BookingHistory::class.java))

                        }
//                        getCurrentLocation()
                        for (i in list){
                            var slot = i.slot
                            var time = SimpleDateFormat("HHmmss").format(Date())
                            var currenthour = time.substring(0,2)
                            var startTime = ""
                            var endTime = ""

                            if(slot[5] == 'A'){
                                startTime = slot.substring(0,2)
                            }
                            else{
                                startTime = (slot.substring(0,2).toInt() + 12).toString()
                            }
                            if(startTime == "24"){
                                startTime = "12"
                            }
                            if(slot[15] == 'A'){
                                endTime = slot.substring(10,12)
                            }
                            else{
                                endTime = (slot.substring(10,12).toInt() + 12).toString()
                            }
                            if(endTime == "24"){
                                endTime = "12"
                            }

                            if(currenthour.toInt() < startTime.toInt()) {
                            }
                            else if(currenthour.toInt() >= startTime.toInt() && currenthour.toInt() < endTime.toInt()){
                                val id = i.id
                                var facilityType = i.type
                                var facilityName = i.facilityName

                                val auth = FirebaseAuth.getInstance()
                                var c = 0


                                var meters = 10
                                var coef = meters * 0.0000089
                                var new_latitude = 0.00
                                var new_longitude = 0.00
                                fireStore.collection("BookingHistory").document(id).collection("Attendees").whereEqualTo("email",auth.currentUser?.email).get()
                                        .addOnSuccessListener {
                                            if(it.isEmpty){
                                                fireStore.collection(facilityType).whereEqualTo("Name",facilityName).get()
                                                        .addOnSuccessListener {documents->
                                                            for(document in documents) {
                                                                facilityLatitude = document.data.getValue("Latitude").toString().toDouble()
                                                                facilityLongitude = document.data.getValue("Longitude").toString().toDouble()
                                                                var dist = distance(userLatitude.toDouble(),userLongitude.toDouble(),facilityLatitude,facilityLongitude)
                                                                if(dist < 0.2) {
                                                                    ongoingllist.add(i)
                                                                }
                                                            }
                                                            ongoinglist.clear()
                                                            for(x in ongoingllist) {
                                                                ongoinglist.add(x)
                                                            }
                                                            (recyclerView.adapter as MarkAttendanceAdapter).notifyDataSetChanged()
                                                            new_latitude = facilityLatitude + coef
                                                            new_longitude = facilityLongitude + coef / cos(facilityLongitude*0.018)
                                                        }

                                                        .addOnFailureListener{
                                                            Toast.makeText(this@MarkAttendanceActivity,"Error",Toast.LENGTH_LONG).show()
                                                        }
                                            }
                                        }
                            }
                        }
//                        ongoinglist.sortByDescending {it.date}

                    }
                    .addOnFailureListener {
                        Log.e("No","Error")
                    }

            uiThread {

                markAttendanceAdapter.setList(ongoinglist)
            }
        }
    }

    private fun distance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val earthRadius = 3958.75 // in miles, change to 6371 for kilometer output

        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)

        val sindLat = sin(dLat / 2)
        val sindLng = sin(dLng / 2)

        val a = sindLat.pow(2.0) + (sindLng.pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)))

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c // output distance, in MILES
    }

}
