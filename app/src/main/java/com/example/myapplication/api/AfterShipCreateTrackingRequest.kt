package com.example.myapplication.api

data class AfterShipCreateTrackingRequest(
    val tracking: Tracking
) {
    data class Tracking(
        val slug: String,
        val tracking_number: String
    )
}
