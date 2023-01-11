package com.mdp_sustainable_goals.course.student.activity

import android.annotation.SuppressLint
import android.content.Intent
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
import com.mdp_sustainable_goals.course.local_storage.entity.SubmissionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class StudentQuizActivity : AppCompatActivity() {
    var idxModule: Int = -1
    var indexing: Int = 0
    var keyAnswer: Int = -1
    var rbIdx: Int = -1
    var correctCount: Int = 0
    var questionCounter: Int = 1
    var class_id: String = ""

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

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_quiz)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(false)
        customActionBar = actionBar
        customActionBar.title = "Quiz"

        db = AppDatabase.build(this)

        idxModule = intent.getIntExtra("idxModule", -1)
        class_id = intent.getStringExtra("idx")!!
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
            if(questionCounter >= listQuiz.size) {
                if(keyAnswer == trappedId[rbIdx]) {
                    correctCount += 1
                }
                coroutine.launch {
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val currentDate = sdf.format(Date())
                    val score: Int = ((correctCount.toDouble() / (listQuiz.size).toDouble()) * (100).toDouble()).roundToInt()
                    val tempModule = db.moduleDao().get(idxModule)
                    db.submissionDao().insert(
                        SubmissionEntity(
                            null,
                            idxModule,
                            tempModule!!.class_id,
                            score,
                            currentDate
                        )
                    )
                    runOnUiThread {
                        val intent = Intent(this@StudentQuizActivity, FinishQuizActivity::class.java)
                        intent.putExtra("total_question", listQuiz.size)
                        intent.putExtra("correct_answer", correctCount)
                        intent.putExtra("score", score)
                        intent.putExtra("idx", class_id)
                        startActivity(intent)
                        finish()
                    }
                }
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
                if(questionCounter == listQuiz.size) {
                    btnNextQuestion.text = "Finish Quiz"
                }
            }
        }
    }
}
