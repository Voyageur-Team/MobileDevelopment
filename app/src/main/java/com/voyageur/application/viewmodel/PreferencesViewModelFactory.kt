package com.voyageur.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.voyageur.application.data.repository.PreferencesRepository

class PreferencesViewModelFactory(private val firestore: FirebaseFirestore) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreferencesViewModel::class.java)) {
            return PreferencesViewModel(PreferencesRepository(firestore)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
