package com.voyageur.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.DataTrip
import com.voyageur.application.data.model.DataUserEmail
import com.voyageur.application.data.model.Participants
import com.voyageur.application.data.model.ResponseParticipants
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

    private val _userEmail = MutableLiveData<List<DataUserEmail>>()
    val userEmail: LiveData<List<DataUserEmail>> = _userEmail

    private val _addParticipant = MutableLiveData<ResponseParticipants>()
    val addParticipant: LiveData<ResponseParticipants> = _addParticipant

    private val _tripDetail = MutableLiveData<DataTrip>()
    val tripDetail: LiveData<DataTrip> get() = _tripDetail


    fun fetchTripDetail(tripId: String, token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).getTripDetail(tripId)
                if (response.isSuccessful && response.body() != null) {
                    _participants.value = response.body()!!.data.participants
                    _isError.value = false
                } else {
                    _isError.value = true
                }
            } catch (e: Exception) {
                _participants.value = emptyList()
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchUserByEmail(email: String, token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).searchUserByEmail(email)
                if (response.isSuccessful && response.body() != null) {
                    val users = response.body()!!.data
                    if (users.isEmpty()) {
                        _userEmail.value = listOf(DataUserEmail(userName = "No user found with this email", email = "", userId = ""))
                    } else {
                        _userEmail.value = users
                    }
                    _isError.value = false
                } else {
                    _isError.value = true
                }
            } catch (e: Exception) {
                _userEmail.value = emptyList()
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addParticipantsToTrip(tripId: String, addParticipants: Participants, token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).addParticipant(tripId, addParticipants)

                if (response.isSuccessful && response.body() != null) {
                    _message.value = "Participant added successfully!"
                } else {
                    _message.value = response.errorBody()?.string() ?: "An error occurred."
                }
            } catch (e: Exception) {
                _message.value = "Failed to add participant: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
