package com.example.exertion.data.user_table

import com.example.exertion.data.user_table.write_dao.UserWriteDao
import com.example.exertion.data.user_table.read_dao.UserReadDao
import kotlinx.coroutines.flow.Flow

class UserRepo(
    private val userReadDao: UserReadDao,
    private val userWriteDao: UserWriteDao
) {
    // READS
    fun readAllData(): Flow<List<UserTable>> = userReadDao.readAllData()

    fun observeUser(id: Int): Flow<UserTable?> =
        userReadDao.observeUser(id)

    suspend fun getUserByEmail(email: String): UserTable? =
        userReadDao.getUserByEmail(email)

    suspend fun getUserByUsername(username: String): UserTable? =
        userReadDao.getUserByUsername(username)

    suspend fun getUserByEmailOrUsername(identifier: String): UserTable? {
        return userReadDao.getUserByEmail(identifier)
            ?: userReadDao.getUserByUsername(identifier)
    }
    suspend fun upsertUser(user: UserTable) {
        userWriteDao.upsertUser(user)
    }
}