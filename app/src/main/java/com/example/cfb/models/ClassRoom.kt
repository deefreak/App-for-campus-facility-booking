package com.example.cfb.models

import com.google.firebase.firestore.local.QueryPurpose
import java.io.Serializable

data class ClassRoom(val BuildingName: String = "",
                          val Department: String = "",
                          val Name: String="",
                          val Strength: String="") : Serializable