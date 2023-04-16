package com.example.myapplication

import android.util.Log
import android.widget.TextView
import com.example.myapplication.api.AfterShipApiClient
import com.example.myapplication.api.AfterShipTrackingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APIFunctions {

    private fun apiCall() {
        val afterShipApiKey = "asat_731056ab1e1341b39d08b43c90626347" // Replace with your actual AfterShip API key
        val slug = "yunexpress" // Replace with the carrier slug (e.g., "fedex", "ups", "usps")
        val trackingNumber = "YT2309621272077981" // Replace with a valid tracking number

        val apiService = AfterShipApiClient.apiService
        apiService.getTrackingInfo(slug, trackingNumber, afterShipApiKey).enqueue(object :
            Callback<AfterShipTrackingResponse> {
            override fun onResponse(call: Call<AfterShipTrackingResponse>, response: Response<AfterShipTrackingResponse>) {
                if (response.isSuccessful) {
                    val trackingInfo = response.body()
                    val checkpoints = trackingInfo?.data?.tracking?.checkpoints
                    val checkpointsInfo = StringBuilder()

                    checkpoints?.forEach { checkpoint ->
                        checkpointsInfo.append("Message: ${checkpoint.message}\n")
                        checkpointsInfo.append("Checkpoint Time: ${checkpoint.checkpointTime}\n")
                        checkpointsInfo.append("City: ${checkpoint.city ?: "N/A"}\n")
                        checkpointsInfo.append("Country: ${checkpoint.country ?: "N/A"}\n")
                        checkpointsInfo.append("Location: ${checkpoint.location ?: "N/A"}\n")
                        checkpointsInfo.append("\n")
                    }
                    Log.d("MainActivity", "API Success: $trackingInfo")

                } else {

                    Log.e("MainActivity", "API Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<AfterShipTrackingResponse>, t: Throwable) {

                Log.e("MainActivity", "API Failure: ${t.message}")
            }
        })
    }
}