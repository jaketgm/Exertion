package com.example.exertion.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesDataStore(private val context: Context) {

    companion object {
        private val USER_ID = intPreferencesKey("logged_in_user_id")
    }

    // Flow that emits user ID or null
    val userIdFlow: Flow<Int?> = context.dataStore.data
        .map { prefs ->
            prefs[USER_ID]
        }

    // Save the logged-in user ID
    suspend fun setLoggedInUserId(userId: Int) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = userId
        }
    }

    // Clear user ID for logout
    suspend fun clearLoggedInUser() {
        context.dataStore.edit { prefs ->
            prefs.remove(USER_ID)
        }
    }
}