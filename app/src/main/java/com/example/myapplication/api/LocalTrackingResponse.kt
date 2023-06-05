package com.example.myapplication.api

data class LocalTrackingResponse(
    val id: String,
    val trackingNumber: String,
    val `carrier-slug`: String,
    val status: String,
    val estimated_date_delivery: String,
    val checkpoints: List<Checkpoint>
) {
    data class Checkpoint(
        val message: String,
        val checkpointTime: String,
        val city: String,
        val country: String,
        val location: String
    )
}

