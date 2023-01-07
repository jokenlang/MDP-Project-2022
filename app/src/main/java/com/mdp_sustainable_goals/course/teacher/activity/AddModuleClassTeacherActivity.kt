package com.mdp_sustainable_goals.course.teacher.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.dao.ModuleDao
import com.mdp_sustainable_goals.course.local_storage.entity.ModuleEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddModuleClassTeacherActivity : AppCompatActivity() {
    lateinit var db: AppDatabase
    val ioScope = CoroutineScope(Dispatchers.IO)

    private lateinit var moduleDao: ModuleDao

    private lateinit var etNamaModule: EditText
    private lateinit var btnNextModule: Button

    lateinit var idx: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_module_class_teacher)

        idx = intent.getStringExtra("idx")!!
        var id = idx.toInt()

        db = AppDatabase.build(this)

        etNamaModule = findViewById(R.id.etNamaModule)
        btnNextModule = findViewById(R.id.btnNextModule)

        btnNextModule.setOnClickListener {
            if (etNamaModule.text.toString() == "") {
            } else {
                val module = ModuleEntity(
                    module_id = null,
                    module_nama = etNamaModule.text.toString(),
                    class_id = idx.toString().toInt(),
                    module_status = 1,
                )
                ioScope.launch {
                    db?.moduleDao()?.insert(module)
                }
                finish()
                // next jumlah soal dan jawaban
            }
        }
    }
}
