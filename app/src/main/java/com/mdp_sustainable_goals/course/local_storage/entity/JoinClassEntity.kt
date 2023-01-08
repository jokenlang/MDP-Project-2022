package com.mdp_sustainable_goals.course.local_storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "join_class")
data class JoinClassEntity(
    @PrimaryKey(autoGenerate = true)
    var join_class_id: Int?,
    var user_username: String,
    var class_id: Int,
) {
}