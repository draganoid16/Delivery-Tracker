package com.example.myapplication.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.api.AfterShipApiClient
import com.example.myapplication.api.AfterShipTrackingResponse
import com.example.myapplication.databinding.FragmentDashboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        val afterShipApiKey = "asat_731056ab1e1341b39d08b43c90626347" // Replace with your actual AfterShip API key
        val slug = "yunexpress" // Replace with the carrier slug (e.g., "fedex", "ups", "usps")
        val trackingNumber = "YT2309621272077981" // Replace with a valid tracking number
        val textAPI = binding.textView

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
                    textAPI.text = checkpointsInfo.toString()

                } else {
                    textAPI.text = "Error!"
                    Log.e("MainActivity", "API Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<AfterShipTrackingResponse>, t: Throwable) {
                textAPI.text = "Failure!"
                Log.e("MainActivity", "API Failure: ${t.message}")
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}