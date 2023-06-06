package com.example.myapplication.ui.home

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.text.observe(viewLifecycleOwner) {
        // Use this observer if you want to update any UI elements with the data
            val actionBar = (activity as? AppCompatActivity)?.supportActionBar
            actionBar?.title = "Delivery Tracker"
            actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#0047FF")))
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.dropdown_menu_popup_item)
        val options = arrayOf("CTT", "UPS", "DHL")

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_popup_item,
            options
        )

        autoCompleteTextView.setAdapter(adapter)

        //mandar info de tracking para proxima atividade
        val button = view.findViewById<Button>(R.id.button)
        val editText = view.findViewById<EditText>(R.id.editTextTextPersonName)
        button.setOnClickListener {
            val intent = Intent(activity, TrackingActivity::class.java)
            intent.putExtra("carrier_slug", autoCompleteTextView.text.toString())
            intent.putExtra("tracking_number", editText.text.toString())
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}