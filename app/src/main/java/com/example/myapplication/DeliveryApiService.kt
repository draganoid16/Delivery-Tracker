package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DeliveryApiService {
    @GET("your_endpoint_here")
    fun getTrackingInfo(@Query("tracking_number") trackingNumber: String): Call<TrackingInfo>
}
