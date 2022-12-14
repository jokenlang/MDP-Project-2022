package com.example.mdp_project

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username")
    var username : String,
    @ColumnInfo(name = "email")
    var email : String,
    @ColumnInfo(name = "name")
    var name : String,
    @ColumnInfo(name = "password")
    var password : String,
    @ColumnInfo(name = "role")
    var role : String // student / teacher
)