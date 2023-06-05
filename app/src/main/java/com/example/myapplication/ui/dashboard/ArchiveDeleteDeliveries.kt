package com.example.myapplication.ui.dashboard

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.room.Room
import com.example.myapplication.R
import com.example.myapplication.api.LocalTrackingApi
import com.example.myapplication.api.LocalTrackingResponse
import com.example.myapplication.roomdatabase.AppDatabase
import com.example.myapplication.roomdatabase.TrackingInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArchiveDeleteDeliveries : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive_delete_deliveries)
        supportActionBar?.title = "Tracking Saved Info"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#0047FF")))
        val imageView: ImageView = findViewById(R.id.imageView)
        imageView.setOnClickListener {
            finish()  // go back
        }

        // Retrieve the carrier slug and tracking number from the intent extras
        val carrierSlug = intent.getStringExtra("carrier_slug")
        val trackingNumber = intent.getStringExtra("tracking_number")
        apiCall(carrierSlug.toString(), trackingNumber.toString())
    }

    private fun apiCall(predefinedCarrierSlug: String, predefinedTrackingNumber: String) {
        try {
            //IDS
            val localServerUrl = "http://192.168.1.77:5000/"
            val estimatedDeliveryText: TextView = findViewById(R.id.estimated_delivery)
            val trackingTitleText: TextView = findViewById(R.id.tracking_title)
            val trackingNumberText: TextView = findViewById(R.id.tracking_number)
            val relativeLayout: RelativeLayout = findViewById(R.id.relativeLayout)

            trackingTitleText.text = "You are currently tracking"
            trackingNumberText.text = predefinedTrackingNumber
            val errorTextView: TextView =
                findViewById(R.id.error_text) // TextView for displaying error messages
            val errorImageView: ImageView =
                findViewById(R.id.error_image2) // ImageView for displaying an error image


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
                            // Clear the error TextView and ImageView
                            errorTextView.text = ""
                            errorImageView.setImageResource(0)
                            relativeLayout.visibility = View.VISIBLE
                            // Get your parent layout
                            val parentLayout: ConstraintLayout = findViewById(R.id.parent_layout)

                            // Previous TextView's id for constraints setup
                            var previousViewId = R.id.estimated_delivery
                            estimatedDeliveryText.text = trackingInfo?.estimated_date_delivery
                                ?: "No Estimated Delivery" //if null = not found

                            trackingInfo?.checkpoints?.takeLast(2)
                                ?.forEach { checkpoint ->  // Limit to 2 checkpoints

                                    // Create a new ImageView
                                    val statusImageView = ImageView(this@ArchiveDeleteDeliveries).apply {
                                        id = View.generateViewId()
                                        layoutParams = ConstraintLayout.LayoutParams(
                                            convertDpToPx(70), // Update with the size you want in dp
                                            convertDpToPx(70)  // Update with the size you want in dp
                                        ).apply {
                                            // Setup constraints
                                            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                                            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                                            topToBottom = previousViewId
                                            // Adding top margin
                                            topMargin =
                                                convertDpToPx(16) // Modify the number based on how much space you want
                                        }
                                        when (checkpoint.message) {
                                            "Package in transit" -> setImageResource(R.drawable.out_for_delivery)
                                            "Delivered" -> setImageResource(R.drawable.delivered_icon)
                                            else -> setImageResource(R.drawable.default_icon)
                                        }
                                        parentLayout.addView(this)
                                    }

                                    // Create a new TextView for checkpoint time
                                    val timeTextView = TextView(this@ArchiveDeleteDeliveries).apply {
                                        id = View.generateViewId()
                                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                                        setTextColor(Color.BLACK)

                                        layoutParams = ConstraintLayout.LayoutParams(
                                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                                        ).apply {
                                            // Setup constraints
                                            startToStart = statusImageView.id
                                            endToEnd = statusImageView.id
                                            topToBottom = statusImageView.id
                                            topMargin = convertDpToPx(8)
                                        }
                                        text = "${checkpoint.checkpointTime}"
                                        parentLayout.addView(this)
                                    }

                                    // Create a new TextView for message
                                    val messageTextView = TextView(this@ArchiveDeleteDeliveries).apply {
                                        id = View.generateViewId()
                                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                                        setTextColor(Color.BLACK)

                                        layoutParams = ConstraintLayout.LayoutParams(
                                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                                        ).apply {
                                            // Setup constraints
                                            startToStart = timeTextView.id
                                            endToEnd = timeTextView.id
                                            topToBottom = timeTextView.id
                                            topMargin = convertDpToPx(8)

                                        }
                                        text = checkpoint.message
                                        parentLayout.addView(this)
                                    }

                                    // For next checkpoint, chain them under this one
                                    previousViewId = messageTextView.id
                                }

                            Log.d("DashboardFragment", "API Success: $trackingInfo")
                        } else {
                            val errorMessage =
                                "API Error! Tracking not found! | Error Code: ${response.code()} - ${
                                    response.errorBody()?.string()
                                }"
                            // Show error message and image
                            trackingTitleText.text = ""
                            trackingNumberText.text = ""
                            estimatedDeliveryText.text = ""
                            relativeLayout.visibility = View.GONE
                            errorTextView.text =
                                "Tracking not found. Please check the tracking number."
                            errorImageView.setImageResource(R.drawable.error_image)
                            Log.e("DashboardFragment", errorMessage)
                        }
                    }

                    override fun onFailure(call: Call<LocalTrackingResponse>, t: Throwable) {
                        Log.e("DashboardFragment", "API Failure: ${t.message}")
                        trackingTitleText.text = ""
                        trackingNumberText.text = ""
                        estimatedDeliveryText.text = ""
                        relativeLayout.visibility = View.GONE
                        errorTextView.text = "API Failure!"
                        errorImageView.setImageResource(R.drawable.error_image)
                    }
                })
        } catch (e: Exception) {
            Log.e("DashboardFragment", "API Call Error: ${e.message}")
        }
    }

    private fun archiveDelivery(deliveryId: Int) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "tracking_info_database"
        ).build()

        GlobalScope.launch(Dispatchers.IO) {
            // Get the delivery info from the current database
            val delivery = db.trackingInfoDao().getById(deliveryId)

            // Delete the delivery from the current database
            delivery?.let {
                db.trackingInfoDao().delete(it)
            }

            // Save the delivery to the archive database
            val archiveDb = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "archive_tracking_info_database"
            ).build()
            delivery?.let {
                db.trackingInfoDao().insert(it)
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(this@ArchiveDeleteDeliveries, "Delivery Archived", Toast.LENGTH_LONG).show()
                // Perform any other necessary actions after archiving the delivery
            }
        }
    }

    private fun deleteDelivery(deliveryId: Int) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "tracking_info_database"
        ).build()

        GlobalScope.launch(Dispatchers.IO) {
            // Get the delivery info from the current database
            val delivery = db.trackingInfoDao().getById(deliveryId)

            // Delete the delivery from the current database
            delivery?.let {
                db.trackingInfoDao().delete(it)
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(this@ArchiveDeleteDeliveries, "Delivery Deleted", Toast.LENGTH_LONG).show()
                // Perform any other necessary actions after deleting the delivery
            }
        }
    }

    private fun convertDpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }


}
