package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.voyageur.application.data.adapter.TripAdapter
import com.voyageur.application.data.model.DataItem
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.FragmentUpcomingBinding
import com.voyageur.application.viewmodel.PlansViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryFragment : Fragment() {
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
        val adapter = TripAdapter { trip ->
            checkUserIdAndNavigate(trip)
        }
        binding.recyclerView.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshTrips()
        }

        observeViewModel(adapter)
    }

    override fun onResume() {
        super.onResume()
        refreshTrips()
    }

    private fun observeViewModel(adapter: TripAdapter) {
        lifecycleScope.launch {
            val userId = pref.getUserId().first()
            val token = pref.getToken().first()
            plansViewModel.getAllTripsUserId(userId, token)
        }

        plansViewModel.trips.observe(viewLifecycleOwner) { trips ->
            val historyTrips = trips.filter { trip ->
                val tripEndDate = parseDate(trip.tripEndDate)
                val currentDate = Date()
                tripEndDate?.before(currentDate) == true
            }

            if (historyTrips.isEmpty()) {
                binding.tvNoData.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.tvNoData.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(historyTrips)
            }
        }

        plansViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun checkUserIdAndNavigate(trip: DataItem) {
        lifecycleScope.launch {
            val userId = pref.getUserId().first()
            val createdBy = trip.createdBy

            if (userId == createdBy) {
                val intent = Intent(context, InviteActivity::class.java)
                intent.putExtra("TRIP_ID", trip.id)
                intent.putExtra("TRIP_TITLE", trip.title)
                startActivity(intent)
            } else {
                val intent = Intent(context, DetailTripActivity::class.java)
                intent.putExtra("TRIP_ID", trip.id)
                startActivity(intent)
            }
        }
    }

    private fun refreshTrips() {
        lifecycleScope.launch {
            val userId = pref.getUserId().first()
            val token = pref.getToken().first()
            plansViewModel.getAllTripsUserId(userId, token)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun parseDate(dateString: String?): Date? {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateString?.let { format.parse(it) }
        } catch (e: Exception) {
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}