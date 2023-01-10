package com.mdp_sustainable_goals.course.local_storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "class")
class ClassEntity(
    @PrimaryKey(autoGenerate = true)
    var class_id: Int?,
    var class_nama: String,
    var user_username: String,
    var class_bidang_studi: String,
    var class_deskripsi: String,
    var class_status: Int
) {
}
