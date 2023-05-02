package com.example.myapplication.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.api.*
import com.example.myapplication.databinding.FragmentDashboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)


        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        apiCall()
        return root
    }

    private fun apiCall() {
        try {
            val localServerUrl = "http://192.168.1.77:5000/"
            val textAPI = binding.textView
            val predefinedCarrierSlug = "portugal-ctt"
            val predefinedTrackingNumber = "LA114547488PT"

            val retrofit = Retrofit.Builder()
                .baseUrl(localServerUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(LocalTrackingApi::class.java)
            apiService.getTrackingInfo(predefinedCarrierSlug, predefinedTrackingNumber)
                .enqueue(object : Callback<LocalTrackingResponse> {
                    override fun onResponse(
                        call: Call<LocalTrackingResponse>,
                        response: Response<LocalTrackingResponse>
                    ) {
                        if (response.isSuccessful) {
                            val trackingInfo = response.body()
                            val checkpointsInfo = StringBuilder()

                            trackingInfo?.checkpoints?.forEach { checkpoint ->
                                checkpointsInfo.append("Message: ${checkpoint.message}\n")
                                checkpointsInfo.append("Checkpoint Time: ${checkpoint.checkpointTime}\n")
                                checkpointsInfo.append("City: ${checkpoint.city}\n")
                                checkpointsInfo.append("Country: ${checkpoint.country}\n")
                                checkpointsInfo.append("Location: ${checkpoint.location}\n")
                                checkpointsInfo.append("\n")
                            }
                            Log.d("DashboardFragment", "API Success: $trackingInfo")
                            textAPI.text = checkpointsInfo.toString()
                        } else {
                            val errorMessage =
                                "API Error! Tracking not found! | Error Code: ${response.code()} - ${
                                    response.errorBody()?.string()
                                }"
                            Log.e("DashboardFragment", errorMessage)
                            textAPI.text = errorMessage
                        }

                    }

                    override fun onFailure(call: Call<LocalTrackingResponse>, t: Throwable) {
                        textAPI.text = "Failure!"
                        Log.e("DashboardFragment", "API Failure: ${t.message}")
                    }

                })
        } catch (e: Exception) {
            Log.e("DashboardFragment", "API Call Error: ${e.message}")
        }
    }

    /** função para api legitimo não local
    private fun createTracking() {
    val apiKey = "asat_731056ab1e1341b39d08b43c90626347"
    val carrierSlug = "portugal-ctt"
    val trackingNumber = "LA114547488PT"
    val textAPI = binding.textView

    val requestBody = AfterShipCreateTrackingRequest(
    tracking = AfterShipCreateTrackingRequest.Tracking(
    slug = carrierSlug,
    tracking_number = trackingNumber
    )
    )

    val apiService = AfterShipApiClient.apiService
    apiService.createTracking(apiKey, requestBody)
    .enqueue(object : Callback<AfterShipCreateTrackingResponse> {
    override fun onResponse(
    call: Call<AfterShipCreateTrackingResponse>,
    response: Response<AfterShipCreateTrackingResponse>
    ) {
    if (response.isSuccessful) {
    Log.d("DashboardFragment", "API Success")
    textAPI.text = "Tracking criado. Atualiza!"
    apiCall()
    } else {
    Log.e(
    "DashboardFragment",
    "API Error: ${response.code()} - ${response.errorBody()?.string()}"
    )
    }
    }

    override fun onFailure(call: Call<AfterShipCreateTrackingResponse>, t: Throwable) {
    Log.e("DashboardFragment", "API Failure: ${t.message}")
    }
    })
    }
     */


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}