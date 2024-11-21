package com.voyageur.application.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.voyageur.application.R

class UpcomingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
    : View? {
        return inflater.inflate(R.layout.fragment_upcoming, container, false)
    }
}