package com.voyageur.application.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voyageur.application.data.model.LoginDataAccount
import com.voyageur.application.data.model.RegisterDataAccount
import com.voyageur.application.data.model.ResponseLogin
import com.voyageur.application.data.network.ApiConfig
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: MutableLiveData<Boolean> = _isError

    private val _message = MutableLiveData<String>()
    val message: MutableLiveData<String> = _message

    private val _user = MutableLiveData<ResponseLogin>()
    val user: MutableLiveData<ResponseLogin> = _user

    fun getLogin(loginDataAccount: LoginDataAccount) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService("").login(loginDataAccount)
                _user.value = response
                _isError.value = false
                _message.value = "Login berhasil!"
            } catch (e: Exception) {
                _isError.value = true
                _message.value = "Email atau password yang anda masukan salah, silahkan coba lagi!"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getRegister(registerDataAccount: RegisterDataAccount) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService("").register(registerDataAccount)
                _isError.value = false
                _message.value = "Registrasi berhasil!"
            } catch (e: Exception) {
                _isError.value = true
                _message.value = "Registrasi gagal!"
            } finally {
                _isLoading.value = false
            }
        }
    }

}