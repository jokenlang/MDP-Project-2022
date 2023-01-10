package com.mdp_sustainable_goals.course.teacher.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.mdp_sustainable_goals.course.teacher.adapter.ClassModuleTeacherAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModuleTeacherActivity : AppCompatActivity() {
    lateinit var idx: String

    private lateinit var tvClassNameDetailTeacher: TextView
    private lateinit var tvClassBidangDetailTeacher: TextView
    private lateinit var btnAddModulesTeacher: Button
    private lateinit var btnListStudentClassTeacher: Button
    private lateinit var rvModulesTeacher: RecyclerView
    private lateinit var ClassModuleTeacherAdapter: ClassModuleTeacherAdapter
    private lateinit var globalFragment: Fragment
    private val globalBundle: Bundle = Bundle()

    lateinit var db: AppDatabase
    val ioScope = CoroutineScope(Dispatchers.IO)

    var modules: ArrayList<ModuleEntity> = arrayListOf()
    private lateinit var kelas: ClassEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_teacher)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.title = "Daftar Module"

        idx = intent.getStringExtra("idx")!!
        var id = idx.toInt()

        db = AppDatabase.build(this)

        btnAddModulesTeacher = findViewById(R.id.btnAddModulesTeacher)
        rvModulesTeacher = findViewById(R.id.rvModulesTeacher)

        val toAddModule =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data
                    if (data != null) {
                        resetUI()
                    }
                } else if (result.resultCode == Activity.RESULT_CANCELED) {
                }
            }

        btnListStudentClassTeacher = findViewById(R.id.btnListStudentClassTeacher)
        btnListStudentClassTeacher.setOnClickListener {
            val intent =
                Intent(this@ModuleTeacherActivity, ListStudentActivity::class.java)
            intent.putExtra("idx", idx.toString())
            startActivity(intent)
        }
        btnAddModulesTeacher.setOnClickListener {
            val intent = Intent(
                this@ModuleTeacherActivity,
                AddModuleClassTeacherActivity::class.java
            )
            intent.putExtra("idx", idx.toString())
            toAddModule.launch(intent)
        }

        val verticalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvModulesTeacher.layoutManager = verticalLayoutManager

        ioScope.launch {
            resetUI()
            this@ModuleTeacherActivity.runOnUiThread {
                ClassModuleTeacherAdapter =
                    ClassModuleTeacherAdapter(this@ModuleTeacherActivity, modules) { idxModule ->
                        val intent =
                            Intent(this@ModuleTeacherActivity, QuizTeacherActivity::class.java)
                        intent.putExtra("idxModule", idxModule.toString())
                        startActivity(intent)
                    }
                rvModulesTeacher.adapter = ClassModuleTeacherAdapter
                rvModulesTeacher.addItemDecoration(object :
                    DividerItemDecoration(
                        rvModulesTeacher.context, VERTICAL
                    ) {
                    override fun onDraw(
                        c: Canvas,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        // super.onDraw(c, parent, state)
                    }
                })
                ClassModuleTeacherAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun resetUI() {
        ioScope.launch {
            modules.clear()
            modules.addAll(db.moduleDao().getModulesByClass(idx.toInt()))
            kelas = db.classDao().get(idx.toInt())!!
            this@ModuleTeacherActivity.runOnUiThread {
                globalFragment = ClassCardInfoFragment(kelas)
                supportFragmentManager.beginTransaction().replace(R.id.classCardInfoView, globalFragment).commit()
                ClassModuleTeacherAdapter.notifyDataSetChanged()
            }
        }
    }
}
