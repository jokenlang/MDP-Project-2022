package com.mdp_sustainable_goals.course.teacher.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.PilihanEntity
import com.mdp_sustainable_goals.course.local_storage.entity.QuizEntity
import com.mdp_sustainable_goals.course.showCustomToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailQuizActivityTeacher : AppCompatActivity() {
    lateinit var etSoal: EditText
    lateinit var etPil1: EditText
    lateinit var etPil2: EditText
    lateinit var etPil3: EditText
    lateinit var btnSave: Button
    var idxQuiz: Int = -1
    var idxModule: Int = -1
    lateinit var listQuiz: MutableList<QuizEntity>
    lateinit var quiz: QuizEntity
    lateinit var listPilihan: MutableList<PilihanEntity>
    lateinit var listPilihanTemp: MutableList<PilihanEntity>
    private val coroutine = CoroutineScope(Dispatchers.IO)
    private lateinit var db: AppDatabase
    var jawaban: Int = 1

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_quiz_teacher)

        db = AppDatabase.build(this)
        listQuiz = mutableListOf()
        listPilihan = mutableListOf()
        listPilihanTemp = mutableListOf()
        etSoal = findViewById(R.id.etSoalDetailQuizTeacher)
        etPil1 = findViewById(R.id.etPil1DetailQuizTeacher)
        etPil2 = findViewById(R.id.etPil2DetailQuizTeacher)
        etPil3 = findViewById(R.id.etPil3DetailQuizTeacher)
        btnSave = findViewById(R.id.btnSaveDetailQuizTeacher)
        var mode = intent.getStringExtra("mode")!!

        coroutine.launch {
            refreshQuiz()
        }
        if (mode == "edit") {
            // isi inputan sebelumnya
            idxQuiz = intent.getIntExtra("quiz_id", -1)
            coroutine.launch {
                quiz = db.quizDao().get(idxQuiz)!!
                listPilihanTemp.clear()
                listPilihanTemp.addAll(db.pilihanDao().fetchByQuiz(idxQuiz).toMutableList())
                if (quiz.pilihan_id == listPilihanTemp[0].pilihan_id) {
                    jawaban = 1
                    etPil1.setBackgroundResource(R.color.module_blue)
                } else if (quiz.pilihan_id == listPilihanTemp[1].pilihan_id) {
                    jawaban = 2
                    etPil2.setBackgroundResource(R.color.module_blue)
                } else if (quiz.pilihan_id == listPilihanTemp[2].pilihan_id) {
                    jawaban = 3
                    etPil3.setBackgroundResource(R.color.module_blue)
                }
                etSoal.setText(quiz.quiz_nama)
                etPil1.setText(listPilihanTemp[0].pilihan_nama)
                etPil2.setText(listPilihanTemp[1].pilihan_nama)
                etPil3.setText(listPilihanTemp[2].pilihan_nama)
            }
        } else if (mode == "add") {
            idxModule = intent.getIntExtra("module_id", -1)
            jawaban = 1
            etPil1.setBackgroundResource(R.color.module_blue)
        }

        etPil1.setOnClickListener {
            etPil1.setBackgroundResource(R.color.module_blue)
            etPil2.setBackgroundResource(R.color.background)
            etPil3.setBackgroundResource(R.color.background)
            jawaban = 1
        }
        etPil2.setOnClickListener {
            etPil2.setBackgroundResource(R.color.module_blue)
            etPil1.setBackgroundResource(R.color.background)
            etPil3.setBackgroundResource(R.color.background)
            jawaban = 2
        }
        etPil3.setOnClickListener {
            etPil3.setBackgroundResource(R.color.module_blue)
            etPil2.setBackgroundResource(R.color.background)
            etPil1.setBackgroundResource(R.color.background)
            jawaban = 3
        }

        btnSave.setOnClickListener {
            if (mode == "add") {
                if (etSoal.text.isEmpty() || etPil1.text.isEmpty() || etPil2.text.isEmpty() || etPil3.text.isEmpty()) {
                    Toast(this).showCustomToast("Input Error", this, "error")
                } else {
                    coroutine.launch {
                        var quiz = QuizEntity(null, etSoal.text.toString(), 1, idxModule, null)
                        db.quizDao().insert(quiz)
                        refreshQuiz()
                        //max kuisnya utk ubah jawaban
                        var max_quiz = listQuiz[listQuiz.size - 1]

                        var pilihan =
                            PilihanEntity(null, etPil1.text.toString(), 1, max_quiz.quiz_id!!)
                        db.pilihanDao().insert(pilihan)
                        refreshQuiz()
                        //cek pilihan 1 adalah jawaban
                        var max_pilihan = listPilihan[listPilihan.size - 1]
                        if (jawaban == 1) {
                            max_quiz.pilihan_id = max_pilihan.pilihan_id
                            db.quizDao().update(max_quiz)
                        }

                        pilihan = PilihanEntity(null, etPil2.text.toString(), 1, max_quiz.quiz_id!!)
                        db.pilihanDao().insert(pilihan)
                        refreshQuiz()

                        //cek pilihan 2 adalah jawaban
                        max_pilihan = listPilihan[listPilihan.size - 1]
                        if (jawaban == 2) {
                            max_quiz.pilihan_id = max_pilihan.pilihan_id
                            db.quizDao().update(max_quiz)
                        }
                        pilihan = PilihanEntity(null, etPil3.text.toString(), 1, max_quiz.quiz_id!!)
                        db.pilihanDao().insert(pilihan)
                        refreshQuiz()

                        //cek pilihan 3 adalah jawaban
                        max_pilihan = listPilihan[listPilihan.size - 1]
                        if (jawaban == 3) {
                            max_quiz.pilihan_id = max_pilihan.pilihan_id
                            db.quizDao().update(max_quiz)
                        }
                    }
                    Toast(this).showCustomToast("Berhasil tambah quiz", this, "success")
//                    finish()
                }
            } else if (mode == "edit") {
                //ganti textnya
                listPilihanTemp[0].pilihan_nama = etPil1.text.toString()
                listPilihanTemp[1].pilihan_nama = etPil2.text.toString()
                listPilihanTemp[2].pilihan_nama = etPil3.text.toString()
                quiz.quiz_nama = etSoal.text.toString()
                if (jawaban == 1) {
                    quiz.pilihan_id = listPilihanTemp[0].pilihan_id
                } else if (jawaban == 2) {
                    quiz.pilihan_id = listPilihanTemp[1].pilihan_id

                } else if (jawaban == 3) {
                    quiz.pilihan_id = listPilihanTemp[2].pilihan_id
                }
                coroutine.launch {
                    db.quizDao().update(quiz)
                    for (i in 0 until listPilihanTemp.size) {
                        db.pilihanDao().update(listPilihanTemp[i])
                    }
                }
                Toast(this).showCustomToast("Berhasil edit quiz", this, "success")
            }
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    suspend fun refreshQuiz() {
        listQuiz.clear()
        listQuiz.addAll(db.quizDao().fetchByModule(idxModule).toMutableList())
        listPilihan.clear()
        listPilihan.addAll(db.pilihanDao().fetch().toMutableList())
    }
}
