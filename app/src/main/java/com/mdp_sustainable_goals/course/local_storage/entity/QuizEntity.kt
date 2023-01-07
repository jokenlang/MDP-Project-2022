package com.mdp_sustainable_goals.course.local_storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz")
data class QuizEntity(
    @PrimaryKey(autoGenerate = true)
    var quiz_id: Int?,
    var quiz_nama: String,
    var quiz_status: Int,
    var module_id: Int,
    var pilihan_id: Int?,
) {
}
