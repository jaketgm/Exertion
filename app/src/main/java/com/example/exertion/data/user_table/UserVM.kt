package com.example.exertion.data.user_table

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.exertion.data.ExertionDB
import com.example.exertion.data.datastore.UserPreferencesDataStore
import kotlinx.coroutines.flow.Flow

class UserVM(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepo
    private val readAllData: Flow<List<UserTable>>

    init {
        val db = ExertionDB.getDatabase(application)
        val userReadDao = db.userReadDao()
        val userWriteDao = db.userWriteDao()
        repository = UserRepo(userReadDao, userWriteDao)
        readAllData = repository.readAllData()
    }

    fun observeUser(id: Int): Flow<UserTable?> =
        repository.observeUser(id)

    // Create or update a user
    suspend fun upsertUser(user: UserTable): Int {
        return repository.upsertUser(user).toInt()
    }

    // Explicit wrapper for clarity
    suspend fun updateUser(user: UserTable): Int {
        return repository.upsertUser(user).toInt()
    }

    suspend fun createUser(
        username: String?,
        email: String,
        passwordHash: String
    ): Int {
        val newUser = UserTable(
            username = username,
            email = email,
            password_hash = passwordHash,
            created_at = System.currentTimeMillis()
        )
        return upsertUser(newUser)
    }

    suspend fun getUserByEmailOrUsername(identifier: String): UserTable? {
        return repository.getUserByEmailOrUsername(identifier)
    }

    suspend fun logout() {
        val prefs = UserPreferencesDataStore(getApplication())
        prefs.clearLoggedInUser()
    }
}