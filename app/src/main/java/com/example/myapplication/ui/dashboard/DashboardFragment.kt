package com.example.myapplication.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.R
import com.example.myapplication.api.*
import com.example.myapplication.databinding.FragmentDashboardBinding
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

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.recyclerView

        val migration1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the new 'name' column to the existing table
                database.execSQL("ALTER TABLE TrackingInfo ADD COLUMN name TEXT DEFAULT '' NOT NULL")
            }
        }

        val db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java, "tracking_info_database"
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    // Handle onCreate if needed
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    // Handle onOpen if needed
                }
            })
            .addMigrations(migration1to2)
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            val trackingInfoList = db.trackingInfoDao().getAll()

            val limitedList = trackingInfoList.take(6)

            withContext(Dispatchers.Main) {
                val deliveryAdapter = DeliveryAdapter(limitedList)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = deliveryAdapter
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class DeliveryAdapter(private var deliveryList: List<TrackingInfo>) :
    RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    class DeliveryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deliveryText: TextView = itemView.findViewById(R.id.delivery_text)
        val detailsButton: Button = itemView.findViewById(R.id.details_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_delivery, parent, false)
        return DeliveryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        val delivery = deliveryList[position]
        holder.deliveryText.text = delivery.name  // Set the name

        holder.detailsButton.setOnClickListener {
            val delivery = deliveryList[position]
            val context = holder.itemView.context

            // Create an Intent to open the ArchiveDeleteDeliveries activity
            val intent = Intent(context, ArchiveDeleteDeliveries::class.java)

            // Add the carrier slug and tracking number as extras to the intent
            intent.putExtra("carrier_slug", delivery.carrierSlug)
            intent.putExtra("tracking_number", delivery.trackingNumber)

            // Start the ArchiveDeleteDeliveries activity
            context.startActivity(intent)
            if (context is Activity) {
                context.finish()
            }

        }
    }

    override fun getItemCount(): Int = deliveryList.size
}
