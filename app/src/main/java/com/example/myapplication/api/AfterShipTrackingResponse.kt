package com.example.myapplication.api

import com.google.gson.annotations.SerializedName

data class AfterShipTrackingResponse(
    @SerializedName("data") val data: TrackingData
) {
    data class TrackingData(
        @SerializedName("tracking") val tracking: Tracking
    )

    data class Tracking(
        @SerializedName("id") val id: String,
        @SerializedName("tracking_number") val trackingNumber: String,
        @SerializedName("slug") val slug: String,
        @SerializedName("status") val status: String,
        @SerializedName("checkpoints") val checkpoints: List<Checkpoint>
    )

    data class Checkpoint(
        @SerializedName("message") val message: String,
        @SerializedName("checkpoint_time") val checkpointTime: String,
        @SerializedName("city") val city: String?,
        @SerializedName("country") val country: String?,
        @SerializedName("location") val location: String?
    )
}

