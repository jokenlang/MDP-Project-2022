package com.mdp_sustainable_goals.course.local_storage.dao

import androidx.room.*
import com.mdp_sustainable_goals.course.local_storage.entity.ModuleEntity
import com.mdp_sustainable_goals.course.local_storage.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserEntity)

    @Query("select * from users")
    fun getAll(): List<UserEntity>

    @Query("SELECT * FROM users where user_username = :user_username")
    suspend fun getUser(user_username: String): UserEntity?
}
