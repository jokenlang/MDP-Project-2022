package com.mdp_sustainable_goals.course.teacher.activity

import android.graphics.Canvas
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.UserEntity
import com.mdp_sustainable_goals.course.teacher.adapter.ListStudentClassTeacherAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListStudentActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private val ioScope = CoroutineScope(Dispatchers.IO)

    private lateinit var adapter: ListStudentClassTeacherAdapter
    private lateinit var rvListStudentClassTeacher: RecyclerView
    private lateinit var tvListStudentEmpty: TextView

    var list_student: ArrayList<UserEntity> = arrayListOf()

    lateinit var idx: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_student)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.title = "Daftar Siswa"

        idx = intent.getStringExtra("idx")!!

        db = AppDatabase.build(this)
        rvListStudentClassTeacher = findViewById(R.id.rvListStudentClassTeacher)
        tvListStudentEmpty = findViewById(R.id.tvListStudentEmpty)

        val verticalLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvListStudentClassTeacher.layoutManager = verticalLayoutManager

        ioScope.launch {
            resetUI()
            this@ListStudentActivity.runOnUiThread {
                adapter = ListStudentClassTeacherAdapter(this@ListStudentActivity, list_student)
                rvListStudentClassTeacher.adapter = adapter
                rvListStudentClassTeacher.addItemDecoration(object :
                    DividerItemDecoration(
                        rvListStudentClassTeacher.context, VERTICAL
                    ) {
                    override fun onDraw(
                        c: Canvas,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        // super.onDraw(c, parent, state)
                    }
                })
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun resetUI() {
        ioScope.launch {
            list_student.clear()
            list_student.addAll(db.joinClassDao().getByClass(idx))
            this@ListStudentActivity.runOnUiThread {
                if (list_student.size > 0) {
                    rvListStudentClassTeacher.visibility = View.VISIBLE
                    tvListStudentEmpty.visibility = View.GONE
                } else {
                    rvListStudentClassTeacher.visibility = View.GONE
                    tvListStudentEmpty.visibility = View.VISIBLE
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}