package com.mdp_sustainable_goals.course.local_storage.dao

import androidx.room.*
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity

@Dao
interface ClassDao {
    @Insert
    suspend fun insert(classEntity: ClassEntity)

    @Update
    suspend fun update(classEntity: ClassEntity)

    @Delete
    suspend fun delete(classEntity: ClassEntity)

    @Query("SELECT * FROM class")
    suspend fun fetch(): List<ClassEntity>

    @Query("SELECT * FROM class c where class_id in (SELECT class_id FROM join_class where user_username = :username)")
    suspend fun getAllJoined(username : String): List<ClassEntity>

    @Query("SELECT * FROM class c where class_id not in (SELECT class_id FROM join_class where user_username = :username)")
    suspend fun getAllNotJoined(username : String): List<ClassEntity>

    @Query("SELECT * FROM class where class_id = :class_id")
    suspend fun get(class_id: Int): ClassEntity?

    @Query("select * from class")
    fun getAll(): List<ClassEntity>

    @Query("select * from class where user_username = :user_username")
    fun getByPengajar(user_username: String): List<ClassEntity>
}
