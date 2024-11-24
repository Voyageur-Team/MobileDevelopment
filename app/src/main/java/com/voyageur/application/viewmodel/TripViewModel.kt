package com.voyageur.application.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.CreateTrip
import com.voyageur.application.data.model.ResponseTrip
import com.voyageur.application.data.network.ApiConfig
import kotlinx.coroutines.launch

class TripViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _trip = MutableLiveData<ResponseTrip>()
    val trip: LiveData<ResponseTrip> = _trip

    fun createTrip(createTrip: CreateTrip, token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).createTrip(createTrip)
                if (response.isSuccessful && response.body() != null) {
                    _trip.value = response.body()
                    _isError.value = false
                    _message.value = "Trip berhasil dibuat!"
                } else {
                    _isError.value = true
                    _message.value = response.errorBody()?.string() ?: "Terjadi kesalahan."
                }
            } catch (e: Exception) {
                _isError.value = true
                _message.value = e.localizedMessage ?: "Gagal membuat trip."
                Log.e("TripViewModel", "Error creating trip", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}

