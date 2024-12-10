package com.voyageur.application.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.DividedItenerary
import com.voyageur.application.data.model.DividedIteneraryItems
import com.voyageur.application.data.model.ResponseDividedItenerary
import com.voyageur.application.data.network.ApiConfig
import kotlinx.coroutines.launch

class RecommendationViewModel:ViewModel() {
    private val _isError = MutableLiveData<Boolean>()
    val isError: MutableLiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: MutableLiveData<String> = _message

    private val _recommendations = MutableLiveData<List<DividedIteneraryItems>>()
    val recommendations: MutableLiveData<List<DividedIteneraryItems>> = _recommendations

    private val _days = MutableLiveData<List<DividedItenerary>>()
    val days: MutableLiveData<List<DividedItenerary>> = _days

    fun getRecommendations(token: String, tripId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService(token).finalizeVoting(tripId)
                if (response.isSuccessful && response.body() != null) {
                    val responseBody: ResponseDividedItenerary = response.body()!!
                    val items = responseBody.data.flatMap { it.items }
                    _recommendations.value = items
                    _days.value = responseBody.data
                    _isError.value = false
                } else {
                    _isError.value = true
                    _message.value = "Failed to fetch data"
                }
            } catch (e: Exception) {
                _isError.value = true
                _message.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }
}
