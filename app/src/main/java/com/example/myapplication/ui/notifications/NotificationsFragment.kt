package com.example.myapplication.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentNotificationsBinding
import com.example.myapplication.roomdatabase.AppDatabase
import com.example.myapplication.roomdatabase.TrackingInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)


        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textView
        notificationsViewModel.text.observe(viewLifecycleOwner) {
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar = (activity as? AppCompatActivity)?.supportActionBar
        actionBar?.title = "Archived Deliveries"
        val recyclerView = binding.recyclerViewNotifications


        val db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java, "archive_tracking_info_database"
        ).build()

        GlobalScope.launch(Dispatchers.IO) {
            val trackingInfoList = db.trackingInfoDao().getAll()

            withContext(Dispatchers.Main) {
                val deliveryAdapter = DeliveryAdapter(trackingInfoList)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = deliveryAdapter
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class DeliveryAdapter(private var deliveryList: List<TrackingInfo>) :
        RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

        class DeliveryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val deliveryText: TextView = itemView.findViewById(R.id.delivery_text)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_archived, parent, false)
            return DeliveryViewHolder(view)
        }

        override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
            val delivery = deliveryList[position]
            holder.deliveryText.text = delivery.name  // Set the name
        }

        override fun getItemCount(): Int = deliveryList.size
    }


}

