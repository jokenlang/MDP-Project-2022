package com.example.mdp_project

import androidx.room.*

@Dao
interface ClassDao {
    @Insert
    suspend fun insert(classEntity: ClassEntity)

    @Update
    suspend fun update(classEntity: ClassEntity)

    @Delete
    suspend fun delete(classEntity: ClassEntity)

    @Query("SELECT * FROM class")
    suspend fun fetch():List<ClassEntity>

    @Query("SELECT * FROM class where class_id = :class_id")
    suspend fun get(class_id:Int):ClassEntity?

    @Query("select * from class")
    fun getAll(): List<ClassEntity>
}