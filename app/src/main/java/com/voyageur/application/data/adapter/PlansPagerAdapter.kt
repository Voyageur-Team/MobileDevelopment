package com.voyageur.application.data.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.voyageur.application.view.ui.HistoryFragment
import com.voyageur.application.view.ui.OngoingFragment
import com.voyageur.application.view.ui.UpcomingFragment

class PlansPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OngoingFragment()
            1 -> UpcomingFragment()
            2 -> HistoryFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}