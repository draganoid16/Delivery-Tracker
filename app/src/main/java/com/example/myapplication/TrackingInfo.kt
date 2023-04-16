package com.example.myapplication

data class TrackingInfo(
    val trackingNumber: String,
    val carrier: String,
    val status: String,
    val estimatedDeliveryDate: String,
    val lastUpdate: String,
    // Add any other fields you need
)

