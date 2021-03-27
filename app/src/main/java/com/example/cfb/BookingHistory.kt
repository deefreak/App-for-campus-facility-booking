package com.example.cfb

import com.google.firebase.firestore.local.QueryPurpose

data class BookingHistory(val BookedBy: String = "",
                val date: String = "",
                val slot: String ="",
                val name: String = "",
                var facilityName: String = "",var purpose: String = "")