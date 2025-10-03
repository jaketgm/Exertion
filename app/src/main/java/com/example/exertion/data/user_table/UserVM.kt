package com.example.exertion.data.user_table

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.exertion.data.ExertionDB
import kotlinx.coroutines.flow.Flow

class UserVM(application: Application): AndroidViewModel(application) {
    private val read_all_data: Flow<List<UserTable>>
    private val repository: UserRepo

    init {
        val db = ExertionDB.getDatabase(application)
        val userReadDao = db.userReadDao()
        val userWriteDao = db.userWriteDao()
        repository = UserRepo(userReadDao, userWriteDao)
        read_all_data = repository.readAllData()
    }

    fun observeUser(id: Int): Flow<UserTable?> = repository.observeUser(id)

    suspend fun upsertUser(user: UserTable) = repository.upsertUser(user)
}