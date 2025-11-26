package com.example.exertion.data.user_table.read_dao

import androidx.room.Dao
import androidx.room.Query
import com.example.exertion.data.user_table.UserTable
import kotlinx.coroutines.flow.Flow

@Dao
interface UserReadDao {
    @Query(
        "SELECT * FROM user_table WHERE user_id = :id"
    )
    fun observeUser(id: Int): Flow<UserTable?>

    @Query(
        "SELECT * FROM user_table WHERE username = :username"
    )
    fun observeByUsername(username: String): Flow<UserTable>

    @Query(
        "SELECT * FROM user_table WHERE email = :email LIMIT 1"
    )
    suspend fun getUserByEmail(email: String): UserTable?

    @Query(
        "SELECT * FROM user_table WHERE username = :username LIMIT 1"
    )
    suspend fun getUserByUsername(username: String): UserTable?

    @Query(
        "SELECT * FROM user_table ORDER BY user_id ASC"
    )
    fun readAllData(): Flow<List<UserTable>>
}