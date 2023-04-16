package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        var button = findViewById<Button>(R.id.button2)

        button.setOnClickListener(){
            apiCall()
        }
    }

    fun apiCall() {
        val resultTextView = findViewById<TextView>(R.id.apiTest)
        val apiService = ApiClient.apiService
        apiService.getTrackingInfo("your_tracking_number_here").enqueue(object : Callback<TrackingInfo> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<TrackingInfo>, response: Response<TrackingInfo>) {
                if (response.isSuccessful) {
                    val trackingInfo = response.body()
                    resultTextView.text = trackingInfo.toString()
                    Log.d("MainActivity", "API Success: $trackingInfo")
                } else {
                    resultTextView.text = "Error: ${response.code()}"
                    Log.e("MainActivity", "API Error: ${response.code()}")
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<TrackingInfo>, t: Throwable) {
                resultTextView.text = "Failure: ${t.message}"
                Log.e("MainActivity", "API Failure: ${t.message}")
            }
        })
    }

}