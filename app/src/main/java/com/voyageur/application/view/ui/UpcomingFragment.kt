package com.voyageur.application.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.voyageur.application.data.adapter.TripAdapter
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.FragmentUpcomingBinding
import com.voyageur.application.viewmodel.PlansViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private val plansViewModel: PlansViewModel by viewModels()
    private lateinit var pref: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = AppPreferences.getInstance(requireContext().dataStore)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = TripAdapter()
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            val userId = pref.getUserId().first()
            val token = pref.getToken().first()
            println("User ID: $userId, Token: $token")

        if (userId != null && token != null) {
                plansViewModel.getAllTripsUserId(userId, token)
            } else {
                Toast.makeText(context, "User ID or Token not found. Please log in again.", Toast.LENGTH_SHORT).show()
            }
        }

        plansViewModel.trips.observe(viewLifecycleOwner) { trips ->
                adapter.submitList(trips)
        }


        plansViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

//        plansViewModel.isError.observe(viewLifecycleOwner) { isError ->
//            if (isError) {
//                Toast.makeText(context, plansViewModel.message.value, Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        plansViewModel.message.observe(viewLifecycleOwner) { message ->
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}