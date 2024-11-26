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
        val adapter = TripAdapter { trip ->
            val intent = Intent(context, InviteActivity::class.java)
            intent.putExtra("TRIP_ID", trip.id)
            intent.putExtra("TRIP_TITLE", trip.title)
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            val userId = pref.getUserId().first()
            val token = pref.getToken().first()
            plansViewModel.getAllTripsUserId(userId, token)
        }

        plansViewModel.trips.observe(viewLifecycleOwner) { trips ->
            adapter.submitList(trips)
        }

        plansViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
