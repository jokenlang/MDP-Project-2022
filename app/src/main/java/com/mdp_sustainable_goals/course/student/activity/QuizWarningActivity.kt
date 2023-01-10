package com.mdp_sustainable_goals.course.student.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.QuizEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizWarningActivity : AppCompatActivity() {
    var idxModule: Int = -1

    private lateinit var tvTotalQuestions: TextView
    private lateinit var btnStartQuiz: Button
    private lateinit var listQuiz: MutableList<QuizEntity>

    private lateinit var db: AppDatabase
    private val coroutine = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_warning)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.title = "Start Quiz"

        db = AppDatabase.build(this)

        idxModule = intent.getIntExtra("idxModule", -1)
        tvTotalQuestions = findViewById(R.id.tvTotalQuestions)
        btnStartQuiz = findViewById(R.id.btnStartQuiz)
        listQuiz = mutableListOf()

        coroutine.launch {
            listQuiz.clear()
            listQuiz.addAll(db.quizDao().fetchByModule(idxModule).toMutableList())
            tvTotalQuestions.text = "Questions in Total: ${listQuiz.size}"
        }

        btnStartQuiz.setOnClickListener {
            println(idxModule)
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
