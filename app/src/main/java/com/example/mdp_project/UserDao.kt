package com.example.mdp_project

import androidx.room.*

@Dao
interface UserDao {
    @Query("select * from users")
    fun getAll(): List<UserEntity>

    @Insert
    suspend fun insert(user:UserEntity)
}
