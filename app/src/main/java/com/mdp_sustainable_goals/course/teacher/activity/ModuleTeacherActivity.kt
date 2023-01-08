package com.mdp_sustainable_goals.course.teacher.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.ModuleEntity
import com.mdp_sustainable_goals.course.teacher.adapter.ClassModuleTeacherAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModuleTeacherActivity : AppCompatActivity() {
    lateinit var idx: String

    private lateinit var btnAddModulesTeacher: Button
    private lateinit var btnListStudentClassTeacher: Button
    private lateinit var rvModulesTeacher: RecyclerView
    private lateinit var ClassModuleTeacherAdapter: ClassModuleTeacherAdapter

    lateinit var db: AppDatabase
    val ioScope = CoroutineScope(Dispatchers.IO)

    var modules: ArrayList<ModuleEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_teacher)

        idx = intent.getStringExtra("idx")!!
        var id = idx.toInt()

        db = AppDatabase.build(this)

        btnAddModulesTeacher = findViewById(R.id.btnAddModulesTeacher)
        rvModulesTeacher = findViewById(R.id.rvModulesTeacher)

        val toAddModule = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result: ActivityResult ->
            if(result.resultCode == RESULT_OK){
                val data = result.data
                if(data != null){
                    resetUI()
                }
            }
            else if (result.resultCode == Activity.RESULT_CANCELED){

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

            this@ModuleTeacherActivity?.runOnUiThread {
                ClassModuleTeacherAdapter =
                    ClassModuleTeacherAdapter(this@ModuleTeacherActivity, modules) { idxModule ->
                        val intent =
                            Intent(this@ModuleTeacherActivity, QuizTeacherActivity::class.java)
                        intent.putExtra("idxModule", idxModule.toString())
                        startActivity(intent)
                    }
                rvModulesTeacher.adapter = ClassModuleTeacherAdapter
                rvModulesTeacher.addItemDecoration(
                    DividerItemDecoration(
                        rvModulesTeacher.context,
                        DividerItemDecoration.VERTICAL
                    )
                )
                ClassModuleTeacherAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun resetUI() {
        ioScope.launch {
            modules.clear()
            modules.addAll(db.moduleDao().getModulesByClass(idx.toInt()))

            this@ModuleTeacherActivity?.runOnUiThread {
                ClassModuleTeacherAdapter.notifyDataSetChanged()
            }
        }
    }
}
