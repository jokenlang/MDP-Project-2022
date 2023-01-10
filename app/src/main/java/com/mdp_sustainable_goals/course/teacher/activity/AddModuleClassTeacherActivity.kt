package com.mdp_sustainable_goals.course.teacher.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.dao.ModuleDao
import com.mdp_sustainable_goals.course.local_storage.entity.ModuleEntity
import com.mdp_sustainable_goals.course.showCustomToast
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

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.title = "Tambah Module"

        btnNextModule.setOnClickListener {
            if (etNamaModule.text.toString() == "") {
                Toast(this@AddModuleClassTeacherActivity).showCustomToast("Input Error",this@AddModuleClassTeacherActivity,"error")
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
                Toast(this@AddModuleClassTeacherActivity).showCustomToast("Module berhasil ditambahkan",this@AddModuleClassTeacherActivity,"success")
                var resultIntent = Intent()
                resultIntent.putExtra("message", "Success add Module")
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
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
