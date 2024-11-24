package com.voyageur.application.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.Cities
import com.voyageur.application.data.model.Preferences
import com.voyageur.application.data.network.ApiService
import kotlinx.coroutines.launch

class PreferencesViewModel(private val apiService: ApiService) : ViewModel() {

    private val _preferences = MutableLiveData<List<Preferences>>()
    val preferences: LiveData<List<Preferences>> = _preferences

    private val _cities = MutableLiveData<List<Cities>>()
    val cities: LiveData<List<Cities>> = _cities

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getAllPreferences() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = apiService.getAllPreferences()
                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    _preferences.postValue(responseBody?.data ?: emptyList())
                    _isError.postValue(responseBody?.error ?: false)
                    _message.postValue(responseBody?.message ?: "Preferences fetched successfully")
                } else {
                    handleError(response.message())
                }
            } catch (e: Exception) {
                handleError(e.localizedMessage ?: "Unknown error occurred while fetching preferences")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getAllCities() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = apiService.getAllCities()
                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    _cities.postValue(responseBody?.data ?: emptyList())
                    _isError.postValue(responseBody?.error ?: false)
                    _message.postValue(responseBody?.message ?: "Cities fetched successfully")
                } else {
                    handleError(response.message())
                }
            } catch (e: Exception) {
                handleError(e.localizedMessage ?: "Unknown error occurred while fetching cities")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun handleError(errorMessage: String) {
        _isError.postValue(true)
        _message.postValue(errorMessage)
        Log.e("PreferencesViewModel", "Error: $errorMessage")
    }
}
