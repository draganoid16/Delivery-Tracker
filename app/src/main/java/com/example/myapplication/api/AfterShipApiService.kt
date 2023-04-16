package com.example.myapplication.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AfterShipApiService {
    @GET("trackings/{slug}/{tracking_number}")
    fun getTrackingInfo(
        @Path("slug") slug: String,
        @Path("tracking_number") trackingNumber: String,
        @Header("aftership-api-key") apiKey: String
    ): Call<AfterShipTrackingResponse>
}

