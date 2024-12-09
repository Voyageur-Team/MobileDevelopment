package com.voyageur.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.DataTrip
import com.voyageur.application.data.model.DataVote
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

    private val _completedParticipants = MutableLiveData<Int>()
    val completedParticipants: LiveData<Int> = _completedParticipants

    private val _totalParticipants = MutableLiveData<Int>()
    val totalParticipants: LiveData<Int> = _totalParticipants

    private val _tripDetail = MutableLiveData<DataTrip>()
    val tripDetail: LiveData<DataTrip> get() = _tripDetail

    private val _userVote = MutableLiveData<Boolean>()
    val userVote: LiveData<Boolean> get() = _userVote

    private val _iteneraryUser = MutableLiveData<DataVote>()
    val iteneraryUser: LiveData<DataVote> get() = _iteneraryUser


    fun getSizeParticipants(tripId: String, token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).getParticipants(tripId)
                if (response.isSuccessful && response.body() != null) {
                    val participants = response.body()?.data?.size ?: 0
                    _participantsCount.value = participants
                    _isError.value = false
                } else {
                    _isError.value = true
                }
            } catch (e: Exception) {
                _isError.value = true
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

    fun postMostPreferences(token: String, tripId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).postMostPreferences(tripId)
                _isError.value = !(response.isSuccessful && response.body() != null)
            } catch (e: Exception) {
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun postRecommendations(token: String, tripId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).postRecommendations(tripId)
                _isError.value = !(response.isSuccessful && response.body() != null)
            } catch (e: Exception) {
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun checkUserAlreadyVoting(token: String, tripId: String, userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).checkUserAlreadyVoting(tripId, userId)
                if (response.isSuccessful && response.body() != null) {
                    _userVote.value = response.body()?.error
                    _isError.value = false
                } else {
                    _isError.value = true
                }
            } catch (e: Exception) {
                _isError.value = true
                _message.value = "Failed to fetch data: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getIteneraryUserId(token: String, tripId: String, userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).checkUserAlreadyVoting(tripId, userId)
                if (response.isSuccessful && response.body() != null) {
                    _iteneraryUser.postValue(response.body()?.data)
                    _isError.value = response.body()?.error ?: false
                    _message.value = response.body()?.message ?: "An error occurred."
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

    fun calculateParticipantProgress(token: String, tripId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).getParticipantProgress(tripId)
                if (response.isSuccessful && response.body() != null) {
                    val progressData = response.body()?.data
                    _completedParticipants.value = progressData?.completedParticipants ?: 0
                    _totalParticipants.value = progressData?.totalParticipants ?: 0
                    _isError.value = response.body()?.error ?: false
                } else {
                    _isError.value = true
                }
            } catch (e: Exception) {
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}
