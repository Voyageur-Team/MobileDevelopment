package com.voyageur.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.voyageur.application.data.model.User
import com.voyageur.application.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    val userLiveData: LiveData<User?> = authRepository.userLiveData
    val authState: LiveData<FirebaseUser?> = authRepository.authState

    fun getCurrentUser() {
        authRepository.getCurrentUser()
    }

    fun saveUserToFirestore(user: FirebaseUser) {
        viewModelScope.launch {
            authRepository.saveUserToFirestore(user)
        }
    }

    fun fetchUserFromFirestore(userId: String) {
        viewModelScope.launch {
            authRepository.fetchUserFromFirestore(userId)
        }
    }
}
