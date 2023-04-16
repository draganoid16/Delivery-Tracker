package com.example.myapplication.api

import retrofit2.Call
import retrofit2.http.*

interface AfterShipApiService {
    @GET("trackings/{slug}/{tracking_number}")
    fun getTrackingInfo(
        @Path("slug") slug: String,
        @Path("tracking_number") trackingNumber: String,
        @Header("aftership-api-key") apiKey: String
    ):Call<AfterShipTrackingResponse>
    @POST("trackings")
    fun createTracking(
        @Header("aftership-api-key") apiKey: String,
        @Body requestBody: AfterShipCreateTrackingRequest
    ): Call<AfterShipCreateTrackingResponse>
}

