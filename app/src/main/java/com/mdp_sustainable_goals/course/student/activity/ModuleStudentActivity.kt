package com.mdp_sustainable_goals.course.student.activity

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.MenuItem
import android.widget.ScrollView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.ClassCardInfoFragment
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
import com.mdp_sustainable_goals.course.local_storage.entity.ModuleEntity
import com.mdp_sustainable_goals.course.student.adapter.ClassModuleStudentAdapter
import com.mdp_sustainable_goals.course.teacher.activity.QuizTeacherActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModuleStudentActivity : AppCompatActivity() {
    lateinit var idx: String

    private lateinit var rvModulesStudent: RecyclerView
    private lateinit var classModuleStudentAdapter: ClassModuleStudentAdapter
    private lateinit var globalFragment: Fragment
    private val globalBundle: Bundle = Bundle()

    lateinit var db: AppDatabase
    val ioScope = CoroutineScope(Dispatchers.IO)

    var modules: ArrayList<ModuleEntity> = arrayListOf()
    private lateinit var kelas: ClassEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_student)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.title = "Daftar Module"

        idx = intent.getStringExtra("idx")!!
        var id = idx.toInt()

        db = AppDatabase.build(this)

        rvModulesStudent = findViewById(R.id.rvModulesStudent)

        val verticalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvModulesStudent.layoutManager = verticalLayoutManager

        ioScope.launch {
            this@ModuleStudentActivity.runOnUiThread {
                classModuleStudentAdapter =
                    ClassModuleStudentAdapter(this@ModuleStudentActivity, modules) { idxModule ->
                        val intent =
                            Intent(this@ModuleStudentActivity, QuizWarningActivity::class.java)
                        intent.putExtra("idxModule", idxModule)
                        intent.putExtra("idx", idx)
                        startActivity(intent)
                        finish()
                    }
                rvModulesStudent.adapter = classModuleStudentAdapter
                rvModulesStudent.addItemDecoration(object :
                    DividerItemDecoration(
                        rvModulesStudent.context, VERTICAL
                    ) {
                    override fun onDraw(
                        c: Canvas,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        // super.onDraw(c, parent, state)
                    }
                })
                rvModulesStudent.setOnTouchListener { v, event ->
                    findViewById<ScrollView>(R.id.scrollView).getParent()
                        .requestDisallowInterceptTouchEvent(false)
                    false
                }
                classModuleStudentAdapter.notifyDataSetChanged()
            }
            modules.clear()
            modules.addAll(db.moduleDao().getModulesActiveModule(idx.toInt()))
            kelas = db.classDao().get(idx.toInt())!!
            this@ModuleStudentActivity.runOnUiThread {
                globalFragment = ClassCardInfoFragment(kelas)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.classCardInfoView2, globalFragment).commit()
                classModuleStudentAdapter.notifyDataSetChanged()
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

    private fun resetUI() {
        ioScope.launch {
            modules.clear()
            modules.addAll(db.moduleDao().getModulesActiveModule(idx.toInt()))
            kelas = db.classDao().get(idx.toInt())!!
            this@ModuleStudentActivity.runOnUiThread {
                globalFragment = ClassCardInfoFragment(kelas)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.classCardInfoView2, globalFragment).commit()
                classModuleStudentAdapter.notifyDataSetChanged()
            }
        }
    }
}
