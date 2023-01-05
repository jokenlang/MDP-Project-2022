package com.example.mdp_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ModuleTeacherActivity : AppCompatActivity() {

    lateinit var idx: String

    private lateinit var btnAddModulesTeacher: Button
    private lateinit var rvModulesTeacher: RecyclerView

    lateinit var db: AppDatabase
    val ioScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_teacher)

        idx = intent.getStringExtra("idx")!!
        var id = idx.toInt()

        db = AppDatabase.build(this)

        btnAddModulesTeacher = findViewById(R.id.btnAddModulesTeacher)
        rvModulesTeacher = findViewById(R.id.rvModulesTeacher)

        btnAddModulesTeacher.setOnClickListener {
            val intent = Intent(this@ModuleTeacherActivity, AddModuleClassTeacherActivity::class.java)
            intent.putExtra("idx", idx.toString())
            startActivity(intent)
        }
    }
}