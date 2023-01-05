package com.example.mdp_project

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailQuizActivityTeacher : AppCompatActivity() {

    lateinit var etSoal : EditText
    lateinit var etPil1 : EditText
    lateinit var etPil2 : EditText
    lateinit var etPil3 : EditText
    lateinit var btnSave : Button
    var idxQuiz : Int = -1
    var idxModule : Int = -1
    lateinit var listQuiz : MutableList<QuizEntity>
    lateinit var listPilihan : MutableList<PilihanEntity>
    private val coroutine = CoroutineScope(Dispatchers.IO)
    private lateinit var db: AppDatabase
    var jawaban : Int  = 1

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_quiz_teacher)

        db = AppDatabase.build(this)
        listQuiz = mutableListOf()
        listPilihan = mutableListOf()
        etSoal = findViewById(R.id.etSoalDetailQuizTeacher)
        etPil1 = findViewById(R.id.etPil1DetailQuizTeacher)
        etPil2 = findViewById(R.id.etPil2DetailQuizTeacher)
        etPil3 = findViewById(R.id.etPil3DetailQuizTeacher)
        btnSave = findViewById(R.id.btnSaveDetailQuizTeacher)
        var mode = intent.getStringExtra("mode")!!

        jawaban = 1
        etPil1.setBackgroundResource(R.color.teal_700)

        coroutine.launch {
            refreshQuiz()
        }
        if (mode == "edit") {
            // isi inputan sebelumnya
            idxQuiz = intent.getIntExtra("quiz_id",-1)
        }
        else if(mode == "add") {
            idxModule = intent.getIntExtra("module_id",-1)
        }

        etPil1.setOnClickListener {
            etPil1.setBackgroundResource(R.color.teal_700)
            etPil2.setBackgroundResource(R.color.white)
            etPil3.setBackgroundResource(R.color.white)
            jawaban = 1
        }
        etPil2.setOnClickListener {
            etPil2.setBackgroundResource(R.color.teal_700)
            etPil1.setBackgroundResource(R.color.white)
            etPil3.setBackgroundResource(R.color.white)
            jawaban = 2
        }
        etPil3.setOnClickListener {
            etPil3.setBackgroundResource(R.color.teal_700)
            etPil2.setBackgroundResource(R.color.white)
            etPil1.setBackgroundResource(R.color.white)
            jawaban = 3
        }

        btnSave.setOnClickListener {
            if(mode == "add") {
                if(etSoal.text.isEmpty() || etPil1.text.isEmpty() || etPil2.text.isEmpty() || etPil3.text.isEmpty()) {
                    Toast(this).showCustomToast("Input Error",this,"error")
                }
                else {
                    coroutine.launch {
                        var quiz = QuizEntity(null, etSoal.text.toString(),1,idxModule,null)
                        db.quizDao().insert(quiz)
                        refreshQuiz()
                        //max kuisnya utk ubah jawaban
                        var max_quiz = listQuiz[listQuiz.size - 1]

                        var pilihan = PilihanEntity(null,etPil1.text.toString(),1,max_quiz.quiz_id!!)
                        db.pilihanDao().insert(pilihan)
                        refreshQuiz()
                        //cek pilihan 1 adalah jawaban
                        var max_pilihan = listPilihan[listPilihan.size - 1]
                        if(jawaban == 1) {
                            max_quiz.pilihan_id = max_pilihan.pilihan_id
                            db.quizDao().update(max_quiz)
                        }

                        pilihan = PilihanEntity(null,etPil2.text.toString(),1,max_quiz.quiz_id!!)
                        db.pilihanDao().insert(pilihan)
                        refreshQuiz()

                        //cek pilihan 2 adalah jawaban
                        max_pilihan = listPilihan[listPilihan.size - 1]
                        if(jawaban == 2) {
                            max_quiz.pilihan_id = max_pilihan.pilihan_id
                            db.quizDao().update(max_quiz)
                        }
                        pilihan = PilihanEntity(null,etPil3.text.toString(),1,max_quiz.quiz_id!!)
                        db.pilihanDao().insert(pilihan)
                        refreshQuiz()

                        //cek pilihan 3 adalah jawaban
                        max_pilihan = listPilihan[listPilihan.size - 1]
                        if(jawaban == 3) {
                            max_quiz.pilihan_id = max_pilihan.pilihan_id
                            db.quizDao().update(max_quiz)
                        }
                    }
                    Toast(this).showCustomToast("Berhasil tambah quiz",this,"success")
                    finish()
                }
            }
            else if(mode == "edit") {

            }
        }
    }


    suspend fun refreshQuiz() {
        listQuiz.clear()
        listQuiz.addAll(db.quizDao().fetchByModule(idxModule).toMutableList())
        listPilihan.clear()
        listPilihan.addAll(db.pilihanDao().fetch().toMutableList())
    }


}