package com.voyageur.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.voyageur.application.data.repository.AppPreferences

class TokenViewModelFactory(private val pref: AppPreferences) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TokenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TokenViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
