package com.example.cfb.models

import com.google.firebase.firestore.local.QueryPurpose

data class BookingHistory(val BookedBy: String = "",
                var date: String = "",
                val slot: String ="",
                val name: String = "",
                var facilityName: String = "",var purpose: String = "",var building: String = "",var type: String="",var id: String="")