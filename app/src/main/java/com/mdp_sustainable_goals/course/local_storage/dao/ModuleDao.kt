package com.mdp_sustainable_goals.course.local_storage.dao

import androidx.room.*
import com.mdp_sustainable_goals.course.local_storage.entity.ModuleEntity

@Dao
interface ModuleDao {
    @Insert
    suspend fun insert(moduleEntity: ModuleEntity)

    @Update
    suspend fun update(moduleEntity: ModuleEntity)

    @Delete
    suspend fun delete(moduleEntity: ModuleEntity)

    @Query("SELECT * FROM module")
    suspend fun fetch(): List<ModuleEntity>

    @Query("SELECT * FROM module where module_id = :module_id")
    suspend fun get(module_id: Int): ModuleEntity?

    @Query("SELECT * FROM module where class_id = :class_id")
    suspend fun getModulesByClass(class_id: Int): List<ModuleEntity>

    @Query("SELECT * FROM module where class_id = :class_id and module_status = 1")
    suspend fun getModulesActiveModule(class_id: Int): List<ModuleEntity>
}
