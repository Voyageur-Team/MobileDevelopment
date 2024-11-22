package com.voyageur.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.Cities
import com.voyageur.application.data.model.Preferences
import com.voyageur.application.data.repository.PreferencesRepository
import kotlinx.coroutines.launch

class PreferencesViewModel(private val repository: PreferencesRepository) : ViewModel() {

    private val _preferences = MutableLiveData<List<Preferences>>()
    val preferences: LiveData<List<Preferences>> get() = _preferences

    private val _cities = MutableLiveData<List<Cities>>()
    val cities: LiveData<List<Cities>> get() = _cities

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message


    fun fetchPreferences() {
        viewModelScope.launch {
            val preferencesList = repository.fetchPreferences()
            _preferences.postValue(preferencesList)
        }
    }

    fun fetchCities() {
        viewModelScope.launch {
            val citiesList = repository.fetchCities()
            _cities.postValue(citiesList)
        }
    }


}
