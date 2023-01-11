package com.mdp_sustainable_goals.course.student.activity

import android.content.Intent
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
    var class_id: String = ""

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
        class_id = intent.getStringExtra("idx")!!

        coroutine.launch {
            listQuiz.clear()
            listQuiz.addAll(db.quizDao().fetchByModule(idxModule).toMutableList())
            // db.submissionDao().nukeTable()
            runOnUiThread {
                if (listQuiz.size > 0) {
                    tvTotalQuestions.text = "Questions in Total: ${listQuiz.size}"
                } else {
                    tvTotalQuestions.text =
                        "Oops, no question for now. Maybe your teacher forgot to put some of it. Get back later!"
                    btnStartQuiz.isEnabled = false
                }
            }
        }

        btnStartQuiz.setOnClickListener {
            val intent =
                Intent(this, StudentQuizActivity::class.java)
            intent.putExtra("idxModule", idxModule)
            intent.putExtra("idx", class_id)
            startActivity(intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, ModuleStudentActivity::class.java)
                intent.putExtra("idx", class_id.toString())
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
