package com.voyageur.application.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.DataItem
import com.voyageur.application.data.network.ApiConfig
import kotlinx.coroutines.launch

class PlansViewModel : ViewModel(){
    private val _isError = MutableLiveData<Boolean>()
    val isError: MutableLiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: MutableLiveData<String> = _message

    private val _trips = MutableLiveData<List<DataItem>>()
    val trips: MutableLiveData<List<DataItem>> = _trips

    fun getAllTripsUserId(userId: String, token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).getTripsByUserId(userId)
                if (response.isSuccessful && response.body() != null) {
                    val userTrip = response.body()!!
                    _trips.value = userTrip.data
                    _isError.value = false
                    _message.value = "Trips berhasil diambil!"
                    println("Data fetched: ${userTrip.data}")
                } else {
                    _isError.value = true
                    _message.value = response.errorBody()?.string() ?: "Terjadi kesalahan."
                }
            } catch (e: Exception) {
                _isError.value = true
                _message.value = e.localizedMessage ?: "Gagal mengambil trips."
            } finally {
                _isLoading.value = false
            }
        }
    }
}