package com.example.mdp_project

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "module")
data class ModuleEntity (
    @PrimaryKey(autoGenerate = true)
    var module_id: Int?,
    var module_nama: String,
    var class_id:Int,
    var module_status:Int,
){
}