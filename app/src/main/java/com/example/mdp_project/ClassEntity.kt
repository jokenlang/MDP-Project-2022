package com.example.mdp_project

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "class")
class ClassEntity (
    @PrimaryKey (autoGenerate = true)
    var class_id : Int,
    var class_nama : String,
    var user_id : Int,
    var class_status : Int

){


}