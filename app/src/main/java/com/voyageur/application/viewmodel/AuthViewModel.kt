package com.voyageur.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.LoginDataAccount
import com.voyageur.application.data.model.RegisterDataAccount
import com.voyageur.application.data.model.ResponseLogin
import com.voyageur.application.data.network.ApiConfig
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _userLogin = MutableLiveData<ResponseLogin>()
    val userLogin: LiveData<ResponseLogin> = _userLogin

    fun register(registerData: RegisterDataAccount){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService("").register(registerData)
                _isError.value = false
                _message.value = "Registrasi berhasil!"
            } catch (e: Exception) {
                _isError.value = true
                _message.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(loginData: LoginDataAccount){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService("").login(loginData)
                _userLogin.value = response
                _isError.value = false
                _message.value = "Login berhasil!"
            } catch (e: Exception) {
                _isError.value = true
                _message.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
