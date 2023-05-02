package com.example.myapplication.api

// LocalTrackingApi.kt
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LocalTrackingApi {
    @GET("tracking")
    fun getTrackingInfo(
        @Query("carrier_slug") carrierSlug: String,
        @Query("tracking_number") trackingNumber: String
    ): Call<LocalTrackingResponse>
}

