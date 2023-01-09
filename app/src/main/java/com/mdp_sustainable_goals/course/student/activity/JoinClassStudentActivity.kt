package com.mdp_sustainable_goals.course.student.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
import com.mdp_sustainable_goals.course.local_storage.entity.JoinClassEntity
import com.mdp_sustainable_goals.course.local_storage.entity.QuizEntity
import com.mdp_sustainable_goals.course.showCustomToast
import com.mdp_sustainable_goals.course.student.adapter.RVJoinClassStudent
import com.mdp_sustainable_goals.course.teacher.activity.DetailQuizActivityTeacher
import com.mdp_sustainable_goals.course.teacher.adapter.RVQuizTeacherAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JoinClassStudentActivity : AppCompatActivity() {
    lateinit var rvJoinAdapter : RVJoinClassStudent
    lateinit var rvClass : RecyclerView

    lateinit var listClass : MutableList<ClassEntity>
    lateinit var listClassTemp : MutableList<ClassEntity>
    lateinit var listJoinClass : MutableList<JoinClassEntity>
    private val coroutine = CoroutineScope(Dispatchers.IO)
    private lateinit var db: AppDatabase
    var username : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_class_student)
        db = AppDatabase.build(this)
        listClass = mutableListOf()
        listClassTemp = mutableListOf()
        listJoinClass = mutableListOf()
        rvClass = findViewById(R.id.rvJoinClassStudent)

        username = intent.getStringExtra("username")!!
        coroutine.launch {
            refreshClass()
        }
        setRV()
    }
    suspend fun refreshClass() {
        listClass.clear()
        listClass.addAll(db.classDao().fetch().toMutableList())
        listJoinClass.clear()
        listJoinClass.addAll(db.joinClassDao().getByUsername(username).toMutableList())
        //cari yang sama dihapus dari list class
        for(i in 0 until listJoinClass.size) {
            var temp_class = listClass.find {
                it.class_id == listJoinClass[i].class_id
            }
            listClass.remove(temp_class)
        }
    }

    fun setRV() {
        listClassTemp = listClass
        val grid = GridLayoutManager(this,2)
        rvJoinAdapter = RVJoinClassStudent(listClassTemp, R.layout.list_class_join_student, this,db) { id ->
            coroutine.launch {
                var jc = JoinClassEntity(null,username,id)
                db.joinClassDao().insert(jc)
            }
            finish()
        }
        rvClass.adapter = rvJoinAdapter
        rvClass.layoutManager = grid
    }

}