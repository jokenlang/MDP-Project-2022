package com.mdp_sustainable_goals.course.local_storage.dao

import androidx.room.*
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
import com.mdp_sustainable_goals.course.local_storage.entity.JoinClassEntity
import com.mdp_sustainable_goals.course.local_storage.entity.UserEntity

@Dao
interface JoinClassDao {
    @Insert
    suspend fun insert(joinClassEntity: JoinClassEntity)

    @Query("select * from join_class")
    fun getAll(): List<JoinClassEntity>

    @Query("select * from users u join join_class c on c.user_username = u.user_username where class_id = :class_id")
    fun getByClass(class_id: String): List<UserEntity>

    @Query("select * from join_class where user_username = :username")
    fun getByUsername(username: String): List<JoinClassEntity>


}