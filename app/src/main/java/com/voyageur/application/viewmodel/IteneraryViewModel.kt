package com.voyageur.application.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.IteneraryItem
import com.voyageur.application.data.network.ApiConfig
import kotlinx.coroutines.launch

class IteneraryViewModel : ViewModel() {

    private val _iteneraries = MutableLiveData<List<IteneraryItem>>()
    val iteneraries: MutableLiveData<List<IteneraryItem>> = _iteneraries

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: MutableLiveData<Boolean> = _isError

    private val _message = MutableLiveData<String>()
    val message: MutableLiveData<String> = _message

    fun getIteneraries(token: String, tripId: String, iteneraryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).getIteneraries(tripId, iteneraryId)
                    _iteneraries.value = response.body()?.recommendations?.itinerary
                    _isError.value = false
                    _message.value = "Itineraries fetched successfully!"

            } catch (e: Exception) {
                _isError.value = true
                _message.value = "Failed to fetch data: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

}