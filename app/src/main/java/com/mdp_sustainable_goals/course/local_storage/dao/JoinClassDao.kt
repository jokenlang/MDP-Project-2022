package com.mdp_sustainable_goals.course.local_storage.dao

import androidx.room.*
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
import com.mdp_sustainable_goals.course.local_storage.entity.JoinClassEntity

@Dao
interface JoinClassDao {
    @Insert
    suspend fun insert(joinClassEntity: JoinClassEntity)

    @Query("select * from join_class")
    fun getAll(): List<JoinClassEntity>

    @Query("select * from join_class where class_id = :class_id")
    fun getByClass(class_id: String): List<JoinClassEntity>

    @Query("select * from join_class where user_username = :username")
    fun getByUsername(username: String): List<JoinClassEntity>


}