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

    @Query("SELECT * FROM class where class_id = :class_id")
    suspend fun get(class_id: Int): ClassEntity?

    @Query("select * from class")
    fun getAll(): List<ClassEntity>
}
