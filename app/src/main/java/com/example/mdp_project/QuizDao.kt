package com.example.mdp_project

import androidx.room.*

@Dao
interface QuizDao {
    @Insert
    suspend fun insert(quizEntity: QuizEntity)

    @Update
    suspend fun update(quizEntity: QuizEntity)

    @Delete
    suspend fun delete(quizEntity: QuizEntity)


    @Query("SELECT * FROM quiz")
    suspend fun fetch():List<QuizEntity>

    @Query("SELECT * FROM quiz where module_id = :module_id and quiz_status = 1")
    suspend fun fetchByModule(module_id:Int):List<QuizEntity>

    @Query("SELECT * FROM quiz where quiz_id = :quiz_id")
    suspend fun get(quiz_id:Int):QuizEntity?

}