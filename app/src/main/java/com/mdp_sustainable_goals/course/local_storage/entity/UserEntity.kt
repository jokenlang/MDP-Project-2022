package com.mdp_sustainable_goals.course.local_storage.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "user_username")
    var username: String,
    @ColumnInfo(name = "user_email")
    var email: String,
    @ColumnInfo(name = "user_name")
    var name: String,
    @ColumnInfo(name = "user_password")
    var password: String,
    @ColumnInfo(name = "user_role")
    var role: String, // student / teacher
    @ColumnInfo(name = "user_saldo")
    var saldo: Int, // student / teacher
    @ColumnInfo(name = "user_seed")
    var seed: String,
)
