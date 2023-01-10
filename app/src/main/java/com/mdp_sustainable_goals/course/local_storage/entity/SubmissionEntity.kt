package com.mdp_sustainable_goals.course.local_storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "submissions")
data class SubmissionEntity(
    @PrimaryKey(autoGenerate = true)
    var submission_id: Int?,
    var module_id: Int,
    var class_id: Int,
    var submission_score: Int,
    var submission_date: String,
)
