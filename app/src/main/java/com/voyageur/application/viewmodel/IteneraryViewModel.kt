package com.voyageur.application.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.IteneraryItem
import com.voyageur.application.data.network.ApiConfig
import kotlinx.coroutines.launch

class IteneraryViewModel : ViewModel() {

    private val _itineraries = MutableLiveData<List<IteneraryItem>>()
    val itineraries: MutableLiveData<List<IteneraryItem>> = _itineraries

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
                if (response.isSuccessful) {
                    val itineraries = response.body()?.data?.itinerary?.map {
                        IteneraryItem(
                            id = it.id,
                            name = it.name,
                            description = it.description,
                            category = it.category,
                            city = it.city,
                            price = it.price,
                            rating = it.rating,
                            location = it.location
                        )
                    } ?: emptyList()
                    _itineraries.value = itineraries
                    _isError.value = false
                    _message.value = "Itineraries fetched successfully!"
                } else {
                    _isError.value = true
                    _message.value = "Error: ${response.code()} ${response.message()}"
                }
            } catch (e: Exception) {
                _isError.value = true
                _message.value = "Failed to fetch data: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}