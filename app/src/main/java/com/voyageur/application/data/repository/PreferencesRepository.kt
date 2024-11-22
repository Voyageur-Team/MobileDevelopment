package com.voyageur.application.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.voyageur.application.data.model.Cities
import com.voyageur.application.data.model.Preferences
import kotlinx.coroutines.tasks.await

class PreferencesRepository(private val firestore: FirebaseFirestore) {

    suspend fun fetchPreferences(): List<Preferences> {
        return try {
            val result = firestore.collection("preferences").get().await()
            result.map { document ->
                document.toObject(Preferences::class.java).apply { id = document.id }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun fetchCities(): List<Cities> {
        return try {
            val result = firestore.collection("city").get().await()
            result.map { document ->
                document.toObject(Cities::class.java).apply { id = document.id }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
