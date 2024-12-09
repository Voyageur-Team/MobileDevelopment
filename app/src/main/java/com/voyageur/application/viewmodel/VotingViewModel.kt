package com.voyageur.application.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.Iteneraries
import com.voyageur.application.data.network.ApiConfig
import kotlinx.coroutines.launch

class VotingViewModel : ViewModel() {

    private val _itineraries = MutableLiveData<List<Iteneraries>>()
    val itineraries: MutableLiveData<List<Iteneraries>> = _itineraries

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: MutableLiveData<Boolean> = _isError

    private val _message = MutableLiveData<String>()
    val message: MutableLiveData<String> = _message

    fun getRecommendations(token: String, tripId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).getRecommendations(tripId)
                if (response.isSuccessful) {
                    _itineraries.value = response.body()?.recommendations
                    _isError.value = false
                    _message.value = "Recommendations fetched successfully!"
                } else {
                    _isError.value = true
                    _message.value = "Error: ${response.code()} ${response.message()}"
                    Log.e("VotingViewModel", "API Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                _isError.value = true
                _message.value = "Failed to fetch data: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun userVote(token: String, tripId: String, userId: String, iteneraryId: String){
        viewModelScope.launch{
            _isLoading.value = true
            try{
                val response = ApiConfig.getApiService(token).voteItenerary(tripId, userId, iteneraryId)
                if(response.isSuccessful){
                    _isError.value = response.body()?.error
                    _message.value = response.body()?.message
                } else {
                    _isError.value = true
                    _message.value = "Error: ${response.code()} ${response.message()}"
                    Log.e("VotingViewModel", "API Error: ${response.code()} ${response.message()}")
                }

            } catch (e: Exception){
                _isError.value = true
                _message.value = "Failed to fetch data: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

}
