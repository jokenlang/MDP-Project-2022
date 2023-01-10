package com.mdp_sustainable_goals.course.local_storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "certificate")
data class CertificateEntity(
    @PrimaryKey(autoGenerate = true)
    var certificate_id: Int?,
    var certificate_number: String,
    var user_username: String,
    var class_id: Int,
    var user_nama: String,
    var module_nama: String,
    var issued_date: String,
)
