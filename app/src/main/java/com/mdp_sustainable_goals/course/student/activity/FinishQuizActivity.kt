package com.mdp_sustainable_goals.course.student.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.mdp_sustainable_goals.course.R

class FinishQuizActivity : AppCompatActivity() {
    private lateinit var tvResults: TextView
    private lateinit var tvCorrectCount: TextView
    private lateinit var tvScore: TextView
    private lateinit var btnCloseQuiz: Button
    var class_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_quiz)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(false)
        actionBar.title = "Quiz"

        tvResults = findViewById(R.id.tvResults)
        tvCorrectCount = findViewById(R.id.tvCorrectCount)
        tvScore = findViewById(R.id.tvScore)
        btnCloseQuiz = findViewById(R.id.btnCloseQuiz)
        class_id = intent.getStringExtra("idx")!!

        tvResults.text = "Questions in Total: ${intent.getIntExtra("total_question", -1)}"
        tvCorrectCount.text = "Correct Answer: ${intent.getIntExtra("correct_answer", -1)}"
        tvScore.text = "Your Score: ${intent.getIntExtra("score", -1)}"

        btnCloseQuiz.setOnClickListener {
            val intent = Intent(this, ModuleStudentActivity::class.java)
            intent.putExtra("idx", class_id)
            startActivity(intent)
            finish()
        }
    }
}
