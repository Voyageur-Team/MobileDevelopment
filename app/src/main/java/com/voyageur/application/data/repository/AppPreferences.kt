package com.voyageur.application.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val USER_ID = stringPreferencesKey("user_id")
    private val LOGIN_SESSION = booleanPreferencesKey("login_session")
    private val TOKEN = stringPreferencesKey("token")
    private val NAME = stringPreferencesKey("userName")
    private val EMAIL = stringPreferencesKey("email")
    private val TUJUAN1 = stringPreferencesKey("tujuan1")
    private val TUJUAN2 = stringPreferencesKey("tujuan2")
    private val DATE_RANGE = stringPreferencesKey("date_range")
    private val BUDGET_MIN = stringPreferencesKey("budget_min")
    private val BUDGET_MAX = stringPreferencesKey("budget_max")
    private val SELECTED_PREFERENCES = stringPreferencesKey("selected_preferences")

    fun getLoginSession(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[LOGIN_SESSION] ?: false
        }
    }

    suspend fun saveLoginSession(loginSession: Boolean) {
        dataStore.edit { preferences ->
            preferences[LOGIN_SESSION] = loginSession
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    fun getName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[NAME] ?: ""
        }
    }

    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[NAME] = name
        }
    }

    fun getUserId(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USER_ID] ?: ""
        }
    }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    fun getEmail(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[EMAIL] ?: ""
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL] = email
        }
    }

    fun getTujuan1(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TUJUAN1] ?: ""
        }
    }

    suspend fun saveTujuan1(tujuan1: String) {
        dataStore.edit { preferences ->
            preferences[TUJUAN1] = tujuan1
        }
    }

    fun getTujuan2(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TUJUAN2] ?: ""
        }
    }

    suspend fun saveTujuan2(tujuan2: String) {
        dataStore.edit { preferences ->
            preferences[TUJUAN2] = tujuan2
        }
    }

    fun getDateRange(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[DATE_RANGE] ?: ""
        }
    }

    suspend fun saveDateRange(dateRange: String) {
        dataStore.edit { preferences ->
            preferences[DATE_RANGE] = dateRange
        }
    }

    fun getBudgetMin(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[BUDGET_MIN] ?: ""
        }
    }

    suspend fun saveBudgetMin(budgetMin: String) {
        dataStore.edit { preferences ->
            preferences[BUDGET_MIN] = budgetMin
        }
    }

    fun getBudgetMax(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[BUDGET_MAX] ?: ""
        }
    }

    suspend fun saveBudgetMax(budgetMax: String) {
        dataStore.edit { preferences ->
            preferences[BUDGET_MAX] = budgetMax
        }
    }

    fun getSelectedPreferences(): Flow<List<String>> {
        return dataStore.data.map { preferences ->
            preferences[SELECTED_PREFERENCES]?.split(",") ?: emptyList()
        }
    }

    suspend fun saveSelectedPreferences(preferences: List<String>) {
        dataStore.edit { prefs ->
            prefs[SELECTED_PREFERENCES] = preferences.joinToString(",")
        }
    }

    suspend fun clearDataLogin() {
        dataStore.edit { preferences ->
            preferences.remove(LOGIN_SESSION)
            preferences.remove(TOKEN)
            preferences.remove(NAME)
            preferences.remove(EMAIL)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): AppPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = AppPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}