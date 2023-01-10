package com.mdp_sustainable_goals.course.teacher.activity

import android.content.Intent
import android.graphics.Color
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
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.ModuleEntity
import com.mdp_sustainable_goals.course.local_storage.entity.QuizEntity
import com.mdp_sustainable_goals.course.showCustomToast
import com.mdp_sustainable_goals.course.teacher.adapter.RVQuizTeacherAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizTeacherActivity : AppCompatActivity() {
    lateinit var tvModule: TextView
    lateinit var rvQuiz: RecyclerView
    lateinit var btnAdd: Button
    lateinit var btnChangeStatusModule: Button
    var idxModule: Int = -1
    lateinit var listQuiz: MutableList<QuizEntity>
    var rvQuizAdapter: RVQuizTeacherAdapter? = null
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
        btnChangeStatusModule = findViewById(R.id.btnChangeStatusModule)
        rvQuiz = findViewById(R.id.rvQuizTeacher)
        tvModule = findViewById(R.id.tvNamaModuleQuizTeacher)

        coroutine.launch {
            module = db.moduleDao().get(idxModule)!!
            tvModule.text = module.module_nama
            refreshQuiz()

            if (module.module_status == 1){
                btnChangeStatusModule.setBackgroundColor(Color.RED)
                btnChangeStatusModule.setTextColor(Color.WHITE)
                btnChangeStatusModule.setText("Disable")
            }
            else{
                btnChangeStatusModule.setBackgroundColor(Color.GREEN)
                btnChangeStatusModule.setTextColor(Color.BLACK)
                btnChangeStatusModule.setText("Enable")
            }
        }

        addLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
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
            intent.putExtra("module_id", idxModule)
            intent.putExtra("mode", "add")
            addLauncher.launch(intent)
        }

        btnChangeStatusModule.setOnClickListener {
            if (module.module_status == 0){
                module.module_status = 1
            }
            else{
                module.module_status = 0
            }

            coroutine.launch {
                db.moduleDao().update(module)

                module =db.moduleDao().get(idxModule)!!

                this@QuizTeacherActivity?.runOnUiThread {
                    tvModule.text = module.module_nama

                    if (module.module_status == 1){
                        btnChangeStatusModule.setBackgroundColor(Color.RED)
                        btnChangeStatusModule.setTextColor(Color.WHITE)
                        btnChangeStatusModule.setText("Disable")
                    }
                    else{
                        btnChangeStatusModule.setBackgroundColor(Color.GREEN)
                        btnChangeStatusModule.setTextColor(Color.BLACK)
                        btnChangeStatusModule.setText("Enable")
                    }
                }

            }
        }
    }

    suspend fun refreshQuiz() {
        listQuiz.clear()
        listQuiz.addAll(db.quizDao().fetchByModule(idxModule).toMutableList())
    }

    fun setRV() {
        val verLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvQuizAdapter =
            RVQuizTeacherAdapter(listQuiz, R.layout.quiz_list_teacher, this) { id, mode ->
                if (mode == "edit") {
                    var intent = Intent(this, DetailQuizActivityTeacher::class.java)
                    intent.putExtra("quiz_id", id)
                    intent.putExtra("mode", "edit")
                    addLauncher.launch(intent)
                } else if (mode == "delete") {
                    coroutine.launch {
                        var temp = db.quizDao().get(id)!!
                        temp.quiz_status = 0
                        db.quizDao().update(temp)
                        refreshQuiz()
                    }
                    setRV()
                    Toast(this).showCustomToast("Berhasil Delete", this, "success")
                }
            }
        rvQuiz.adapter = rvQuizAdapter
        rvQuiz.layoutManager = verLayoutManager
    }
}
