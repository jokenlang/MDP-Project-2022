package com.mdp_sustainable_goals.course.teacher.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.ModuleEntity
import com.mdp_sustainable_goals.course.local_storage.entity.UserEntity
import com.mdp_sustainable_goals.course.teacher.adapter.ClassDashboardTeacherAdapter
import com.mdp_sustainable_goals.course.teacher.adapter.ClassModuleTeacherAdapter
import com.mdp_sustainable_goals.course.teacher.adapter.ListStudentClassTeacherAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListStudentActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private val ioScope = CoroutineScope(Dispatchers.IO)

    private lateinit var adapter: ListStudentClassTeacherAdapter

    private lateinit var rvListStudentClassTeacher: RecyclerView

    var list_student: ArrayList<UserEntity> = arrayListOf()

    lateinit var idx: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_student)

        idx = intent.getStringExtra("idx")!!

        db = AppDatabase.build(this)
        rvListStudentClassTeacher = findViewById(R.id.rvListStudentClassTeacher)

        val verticalLayoutManager =
            GridLayoutManager(this, 3)
        rvListStudentClassTeacher.layoutManager = verticalLayoutManager

        ioScope.launch {
            resetUI()
            this@ListStudentActivity?.runOnUiThread {
                adapter = ListStudentClassTeacherAdapter(this@ListStudentActivity, list_student)
                rvListStudentClassTeacher.adapter = adapter
                rvListStudentClassTeacher.addItemDecoration(
                    DividerItemDecoration(
                        rvListStudentClassTeacher.context,
                        DividerItemDecoration.VERTICAL
                    )
                )
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun resetUI() {
        ioScope.launch {
            list_student.clear()
            list_student.addAll(db.joinClassDao().getByClass(idx))
            this@ListStudentActivity?.runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }
}