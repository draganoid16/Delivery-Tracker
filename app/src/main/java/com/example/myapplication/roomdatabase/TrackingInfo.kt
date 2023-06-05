package com.example.myapplication.roomdatabase
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrackingInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,  // Add the name property
    val carrierSlug: String,
    val trackingNumber: String
)
