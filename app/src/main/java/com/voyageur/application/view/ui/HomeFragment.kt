package com.voyageur.application.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.voyageur.application.R
import com.voyageur.application.data.adapter.PopularDestinationAdapter
import com.voyageur.application.data.model.PopularDestination
import com.voyageur.application.data.repository.AppPreferences
import com.voyageur.application.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        pref = AppPreferences.getInstance(requireContext().dataStore)

        setUserData()

        binding.btnSetting.setOnClickListener {
            startActivity(Intent(activity, SettingsActivity::class.java))
        }

        binding.btnTrip.setOnClickListener {
            startActivity(Intent(activity, TripActivity::class.java))
        }

        setupRecyclerList()

        return root
    }

    private fun setUserData() {
        pref.getName().asLiveData().observe(viewLifecycleOwner) { userName ->
            if (!userName.isNullOrEmpty()) {
                binding.tvName.text = userName
            } else {
                binding.tvName.text = "User"
            }
        }
    }


    private fun getPopularDestination(): ArrayList<PopularDestination> {
        val dataName = resources.getStringArray(R.array.popular_destination)
        val dataLocation = resources.getStringArray(R.array.popular_destination_location)
        val dataDescription = resources.getStringArray(R.array.popular_destination_description)
        val dataPhoto = resources.obtainTypedArray(R.array.popular_destination_image)
        val dataRating = resources.getStringArray(R.array.popular_destination_rating).map { it.toFloat() }.toTypedArray()
        val dataPrice = resources.getStringArray(R.array.popular_destination_price).map { it.replace("$", "").toFloat() }.toTypedArray()
        val listHero = ArrayList<PopularDestination>()
        for (i in dataName.indices) {
            val hero = PopularDestination(
                name = dataName[i],
                location = dataLocation[i],
                description = dataDescription[i],
                image = dataPhoto.getResourceId(i, -1),
                rating = dataRating[i],
                price = dataPrice[i]
            )
            listHero.add(hero)
        }
        dataPhoto.recycle()
        return listHero
    }

    private fun setupRecyclerList() {
        val list = getPopularDestination()
        binding.rvPopularDestination.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val popularDestinationAdapter = PopularDestinationAdapter(list)
        binding.rvPopularDestination.adapter = popularDestinationAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
