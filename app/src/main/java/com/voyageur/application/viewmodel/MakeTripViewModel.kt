package com.voyageur.application.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.Cities
import com.voyageur.application.data.network.ApiConfig
import kotlinx.coroutines.launch

class MakeTripViewModel : ViewModel() {

    private val _cities = MutableLiveData<List<Cities>>()
    val cities: MutableLiveData<List<Cities>> = _cities

    private val _isError = MutableLiveData<Boolean>()
    val isError: MutableLiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: MutableLiveData<String> = _message

    fun getAllCities(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).getAllCities()
                if (response.isSuccessful && response.body() != null) {
                    _cities.value = response.body()!!.data
                    _isError.value = false
                    _message.value = "Cities fetched successfully!"
                } else {
                    _isError.value = true
                    _message.value = response.errorBody()?.string() ?: "An error occurred."
                }
            } catch (e: Exception) {
                _cities.value = emptyList()
                _isError.value = true
                _message.value = "Failed to fetch data: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
