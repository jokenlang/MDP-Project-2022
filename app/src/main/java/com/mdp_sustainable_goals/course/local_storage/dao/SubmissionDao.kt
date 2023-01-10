package com.mdp_sustainable_goals.course.local_storage.dao

import androidx.room.*
import com.mdp_sustainable_goals.course.local_storage.entity.SubmissionEntity

@Dao
interface SubmissionDao {
    @Insert
    suspend fun insert(submissionEntity: SubmissionEntity)

    @Update
    suspend fun update(submissionEntity: SubmissionEntity)

    @Delete
    suspend fun delete(submissionEntity: SubmissionEntity)

    @Query("SELECT * FROM submissions")
    suspend fun getAll(): List<SubmissionEntity>

    @Query("SELECT * FROM submissions where module_id = :module_id")
    suspend fun getByModuleId(module_id: Int): SubmissionEntity?

    @Query("SELECT * FROM submissions where class_id = :class_id")
    suspend fun getByClassId(class_id: Int): SubmissionEntity?
}
