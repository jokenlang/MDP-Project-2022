package com.example.mdp_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class QuizTeacherActivity : AppCompatActivity() {
    lateinit var tvModule : TextView
    lateinit var rvQuiz : RecyclerView
    lateinit var btnAdd : Button
    var idxModule : Int = -1
    lateinit var listQuiz : MutableList<QuizEntity>
    var rvQuizAdapter: RVQuizTeacherAdapter ?=null
    private val coroutine = CoroutineScope(Dispatchers.IO)
    private lateinit var db: AppDatabase
    private lateinit var module : ModuleEntity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_teacher)
        listQuiz = mutableListOf()

        db = AppDatabase.build(this)
        idxModule = intent.getStringExtra("idx")!!.toInt()
        btnAdd = findViewById(R.id.btnAddQuizTeacher)
        rvQuiz = findViewById(R.id.rvQuizTeacher)
        tvModule = findViewById(R.id.tvNamaModule)

        coroutine.launch {
            refreshQuiz()
        }

        tvModule.text = module.module_nama

        setRV()


    }

    suspend fun refreshQuiz() {
        module = db.moduleDao().get(idxModule)!!
        listQuiz.clear()
        listQuiz.addAll(db.quizDao().fetchByModule(idxModule).toMutableList())
    }

    fun setRV() {
        val verLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rvQuizAdapter = RVQuizTeacherAdapter(listQuiz,R.layout.quiz_list_teacher, this) {id, mode ->
            if(mode == "edit") {
                var intent = Intent(this, DetailQuizActivityTeacher::class.java)
                intent.putExtra("idx",idxModule.toString())
                startActivity(intent)
            }
        }
        rvQuiz.adapter = rvQuizAdapter
        rvQuiz.layoutManager = verLayoutManager
    }
}