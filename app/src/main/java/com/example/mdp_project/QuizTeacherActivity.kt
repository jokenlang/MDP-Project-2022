package com.example.mdp_project

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizTeacherActivity : AppCompatActivity() {
    lateinit var tvModule : TextView
    lateinit var rvQuiz : RecyclerView
    lateinit var btnAdd : Button
    var idxModule : Int = -1
    lateinit var listQuiz : MutableList<QuizEntity>
    var rvQuizAdapter: RVQuizTeacherAdapter ?=null
    private val coroutine = CoroutineScope(Dispatchers.IO)
    private lateinit var db: AppDatabase
    lateinit var module: ModuleEntity
    lateinit var addLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_teacher)
        listQuiz = mutableListOf()

        db = AppDatabase.build(this)
        idxModule = intent.getStringExtra("idxModule")!!.toInt()
//        Toast.makeText(this, "$idxModule", Toast.LENGTH_SHORT).show()
        btnAdd = findViewById(R.id.btnAddQuizTeacher)
        rvQuiz = findViewById(R.id.rvQuizTeacher)
        tvModule = findViewById(R.id.tvNamaModuleQuizTeacher)

        coroutine.launch {
            module = db.moduleDao().get(idxModule)!!
            tvModule.text = module.module_nama
            refreshQuiz()
        }

        addLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                coroutine.launch {
                    refreshQuiz()
                }
                setRV()
            }
        }


        setRV()

        btnAdd.setOnClickListener {
            var intent = Intent(this, DetailQuizActivityTeacher::class.java)
            intent.putExtra("module_id",idxModule)
            intent.putExtra("mode","add")
            addLauncher.launch(intent)
        }

    }

    suspend fun refreshQuiz() {
        listQuiz.clear()
        listQuiz.addAll(db.quizDao().fetchByModule(idxModule).toMutableList())
    }

    fun setRV() {
        val verLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rvQuizAdapter = RVQuizTeacherAdapter(listQuiz,R.layout.quiz_list_teacher, this) {id, mode ->
            if(mode == "edit") {
                var intent = Intent(this, DetailQuizActivityTeacher::class.java)
                intent.putExtra("quiz_id",id)
                intent.putExtra("mode","edit")
                addLauncher.launch(intent)
            }
            else if(mode == "delete") {
                coroutine.launch {
                    var temp = db.quizDao().get(id)!!
                    temp.quiz_status = 0
                    db.quizDao().update(temp)
                    refreshQuiz()
                }
                setRV()

                Toast(this).showCustomToast("Berhasil Delete",this,"success")
            }
        }
        rvQuiz.adapter = rvQuizAdapter
        rvQuiz.layoutManager = verLayoutManager
    }

}