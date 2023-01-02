package com.example.mdp_project

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pilihan")
class PilihanEntity (
    @PrimaryKey(autoGenerate = true)
    var pilihan_id : Int,
    var pilihan_nama : String,
    var pilihan_status : Int,
    var quiz_id : Int,
){
}