package com.mdp_sustainable_goals.course.local_storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "module")
data class ModuleEntity(
    @PrimaryKey(autoGenerate = true)
    var module_id: Int?,
    var module_nama: String,
    var class_id: Int,
    var module_deskripsi: String,
    var module_status: Int,
) {
}
