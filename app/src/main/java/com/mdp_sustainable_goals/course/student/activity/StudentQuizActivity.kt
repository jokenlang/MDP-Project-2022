package com.mdp_sustainable_goals.course.student.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.PilihanEntity
import com.mdp_sustainable_goals.course.local_storage.entity.QuizEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentQuizActivity : AppCompatActivity() {
    var idxModule: Int = -1
    var indexing: Int = 0
    var keyAnswer: Int = -1
    var rbIdx: Int = -1
    var correctCount: Int = 0
    var questionCounter: Int = 1

    private lateinit var customActionBar: ActionBar

    private lateinit var tvSoalDetailQuiz: TextView
    private lateinit var rgPilDetailQuiz: RadioGroup
    private lateinit var rbPil1DetailQuiz: RadioButton
    private lateinit var rbPil2DetailQuiz: RadioButton
    private lateinit var rbPil3DetailQuiz: RadioButton
    private lateinit var btnNextQuestion: Button
    private lateinit var listQuiz: MutableList<QuizEntity>
    private lateinit var listPilihan: MutableList<PilihanEntity>
    private lateinit var trappedId: ArrayList<Int>

    private lateinit var db: AppDatabase
    private val coroutine = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_quiz)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(false)
        customActionBar = actionBar
        customActionBar.title = "Quiz"

        db = AppDatabase.build(this)

        idxModule = intent.getIntExtra("idxModule", -1)
        tvSoalDetailQuiz = findViewById(R.id.tvSoalDetailQuiz)
        rgPilDetailQuiz = findViewById(R.id.rgPilDetailQuiz)
        rbPil1DetailQuiz = findViewById(R.id.rbPil1DetailQuiz)
        rbPil2DetailQuiz = findViewById(R.id.rbPil2DetailQuiz)
        rbPil3DetailQuiz = findViewById(R.id.rbPil3DetailQuiz)
        btnNextQuestion = findViewById(R.id.btnNextQuestion)
        listQuiz = mutableListOf()
        listPilihan = mutableListOf()
        trappedId = arrayListOf()
        trappedId.add(-1)
        trappedId.add(-1)
        trappedId.add(-1)

        coroutine.launch {
            listQuiz.clear()
            listQuiz.addAll(db.quizDao().fetchByModule(idxModule).toMutableList())
            listQuiz.shuffle()
            loadQuiz()
        }

        rbPil1DetailQuiz.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                rbIdx = 0
            }
        }
        rbPil2DetailQuiz.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                rbIdx = 1
            }
        }
        rbPil3DetailQuiz.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                rbIdx = 2
            }
        }

        btnNextQuestion.setOnClickListener {
            println("HEHEHE" + questionCounter)
            if(questionCounter >= listQuiz.size) {
                // selesai
                println("KARENA KALO 4 HEHE")
            } else {
                if(keyAnswer == trappedId[rbIdx]) {
                    correctCount += 1
                }
                questionCounter += 1
                indexing += 1
                rgPilDetailQuiz.clearCheck()
                loadQuiz()
            }
        }
    }

    private fun loadQuiz() {
        coroutine.launch {
            listPilihan.clear()
            listPilihan.addAll(
                db.pilihanDao().fetchByQuiz(listQuiz[indexing].quiz_id!!).toMutableList()
            )
            runOnUiThread {
                customActionBar.title = "Questions ${questionCounter}/${listQuiz.size}"
                tvSoalDetailQuiz.text = listQuiz[indexing].quiz_nama
                keyAnswer = listQuiz[indexing].pilihan_id!!
                listPilihan.shuffle()
                rbPil1DetailQuiz.text = listPilihan[0].pilihan_nama
                trappedId[0] = listPilihan[0].pilihan_id!!
                rbPil2DetailQuiz.text = listPilihan[1].pilihan_nama
                trappedId[1] = listPilihan[1].pilihan_id!!
                rbPil3DetailQuiz.text = listPilihan[2].pilihan_nama
                trappedId[2] = listPilihan[2].pilihan_id!!
            }
        }
    }
}
