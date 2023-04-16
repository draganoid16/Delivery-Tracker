package com.example.myapplication.api

data class AfterShipCreateTrackingResponse(
    val meta: Meta,
    val data: Data
) {
    data class Meta(
        val code: Int
    )

    data class Data(
        val tracking: Tracking
    ) {
        data class Tracking(
            val id: String,
            val tracking_number: String,
            val carrier_slug: String
        )
    }
}
