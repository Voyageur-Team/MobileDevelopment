package com.voyageur.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.Participants
import com.voyageur.application.data.network.ApiConfig
import kotlinx.coroutines.launch

class InviteViewModel : ViewModel() {
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _participants = MutableLiveData<List<Participants>>()
    val participants: LiveData<List<Participants>> = _participants

    fun fetchTripDetail(tripId: String, token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).getTripDetail(tripId)
                if (response.isSuccessful && response.body() != null) {
                    _participants.value = response.body()!!.data.participants
                    _isError.value = false
                    _message.value = "Participants fetched successfully!"
                } else {
                    _isError.value = true
                    _message.value = response.errorBody()?.string() ?: "An error occurred."
                }
            } catch (e: Exception) {
                _participants.value = emptyList()
                _isError.value = true
                _message.value = "Failed to fetch data: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
