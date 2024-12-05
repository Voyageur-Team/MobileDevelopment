package com.voyageur.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.DataTrip
import com.voyageur.application.data.network.ApiConfig
import kotlinx.coroutines.launch

class DetailTripViewModel: ViewModel() {
    private val _isError = MutableLiveData<Boolean>()
    val isError: MutableLiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: MutableLiveData<String> = _message

    private val _participantsCount = MutableLiveData<Int>()
    val participantsCount: MutableLiveData<Int> = _participantsCount

    private val _tripDetail = MutableLiveData<DataTrip>()
    val tripDetail: LiveData<DataTrip> get() = _tripDetail

    fun getSizeParticipants(tripId: String, token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).getParticipants(tripId)
                if (response.isSuccessful && response.body() != null) {
                    val participants = response.body()?.data?.size ?: 0
                    _participantsCount.value = participants
                    _isError.value = false
                    _message.value = "Participants fetched successfully!"
                } else {
                    _isError.value = true
                    _message.value = response.errorBody()?.string() ?: "An error occurred."
                }
            } catch (e: Exception) {
                _isError.value = true
                _message.value = "Failed to fetch data: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getTripDetail(tripId: String, token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).getTripDetail(tripId)
                if (response.isSuccessful && response.body() != null) {
                    _isError.value = false
                    _message.value = "Trip fetched successfully!"
                    _tripDetail.value = response.body()?.data
                } else {
                    _isError.value = true
                    _message.value = response.errorBody()?.string() ?: "An error occurred."
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
