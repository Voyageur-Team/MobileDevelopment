package com.voyageur.application.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.Cities
import com.voyageur.application.data.model.DataParticipantPreferences
import com.voyageur.application.data.model.Participants
import com.voyageur.application.data.model.Preferences
import com.voyageur.application.data.model.UserPreffered
import com.voyageur.application.data.network.ApiConfig
import kotlinx.coroutines.launch

class MakeTripViewModel : ViewModel() {

    private val _cities = MutableLiveData<List<Cities>>()
    val cities: MutableLiveData<List<Cities>> = _cities

    private val _preferences = MutableLiveData<List<Preferences>>()
    val preferences: MutableLiveData<List<Preferences>> = _preferences

    private val _isError = MutableLiveData<Boolean>()
    val isError: MutableLiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: MutableLiveData<String> = _message

    private val _tripDetail = MutableLiveData<List<Participants>>()
    val tripDetail: MutableLiveData<List<Participants>> = _tripDetail

    private val _tripDetailUser = MutableLiveData<UserPreffered>()
    val tripDetailUser: MutableLiveData<UserPreffered> = _tripDetailUser

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

    fun getAllPreferences(token: String){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).getAllPreferences()
                if (response.isSuccessful && response.body() != null) {
                    _preferences.value = response.body()!!.data
                    _isError.value = false
                    _message.value = "Preferences fetched successfully!"
                } else {
                    _isError.value = true
                    _message.value = response.errorBody()?.string() ?: "An error occurred."
                }
            } catch (e: Exception) {
                _preferences.value = emptyList()
                _isError.value = true
                _message.value = "Failed to fetch data: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun postParticipantPreferences(
        token: String,
        tripId: String,
        participantId: String,
        participantPreferences: DataParticipantPreferences
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).addParticipantPreferences(
                    tripId, participantId, participantPreferences
                )
                if (response.isSuccessful && response.body() != null) {
                    _isError.value = false
                    _message.value = "Participant preferences added successfully!"
                } else {
                    _isError.value = true
                    _message.value = response.errorBody()?.string() ?: "An error occurred."
                }
            } catch (e: Exception) {
                _isError.value = true
                _message.value = "Failed to add participant preferences: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getUserPreffered(token: String, tripId: String, userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).checkUserPreferences(tripId, userId)
                if (response.isSuccessful && response.body() != null) {
                    _isError.value = false
                    _message.value = "User preferences fetched successfully!"
                    _tripDetailUser.value = response.body()?.data
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
