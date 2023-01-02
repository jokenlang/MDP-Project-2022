package com.example.mdp_project

import androidx.room.*

@Dao
interface PilihanDao {
    @Insert
    suspend fun insert(pilihanEntity: PilihanEntity)

    @Update
    suspend fun update(pilihanEntity: PilihanEntity)

    @Delete
    suspend fun delete(pilihanEntity: PilihanEntity)

    @Query("SELECT * FROM pilihan")
    suspend fun fetch():List<PilihanEntity>

    @Query("SELECT * FROM pilihan where pilihan_id = :pilihan_id")
    suspend fun get(pilihan_id:Int):PilihanEntity?
}