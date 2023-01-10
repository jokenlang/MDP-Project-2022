package com.mdp_sustainable_goals.course.local_storage.dao

import androidx.room.*
import com.mdp_sustainable_goals.course.local_storage.entity.CertificateEntity

@Dao
interface CertificateDao {
    @Insert
    suspend fun insert(certificateEntity: CertificateEntity)

    @Update
    suspend fun update(certificateEntity: CertificateEntity)

    @Delete
    suspend fun delete(certificateEntity: CertificateEntity)

    @Query("SELECT * FROM certificate where class_id = :class_id and user_username = :user_username")
    suspend fun getByClassId(class_id: Int, user_username: String): CertificateEntity?
}
