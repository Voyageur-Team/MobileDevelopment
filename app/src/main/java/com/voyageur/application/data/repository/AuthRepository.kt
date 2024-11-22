package com.voyageur.application.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.voyageur.application.data.model.User

class AuthRepository {

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _userLiveData = MutableLiveData<User?>()
    val userLiveData: LiveData<User?> = _userLiveData

    private val _authState = MutableLiveData<FirebaseUser?>()
    val authState: LiveData<FirebaseUser?> = _authState

    fun getCurrentUser() {
        _authState.postValue(auth.currentUser)
    }

    fun saveUserToFirestore(user: FirebaseUser) {
        val userData = User(
            userId = user.uid,
            email = user.email ?: "",
            displayName = user.displayName ?: "",
            photoUrl = user.photoUrl.toString()
        )

        db.collection("users").document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                _userLiveData.postValue(userData)
            }
            .addOnFailureListener {
                _userLiveData.postValue(null)
            }
    }

    fun fetchUserFromFirestore(userId: String) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject<User>()
                    _userLiveData.postValue(user)
                } else {
                    _userLiveData.postValue(null)
                }
            }
            .addOnFailureListener {
                _userLiveData.postValue(null)
            }
    }
}
