package com.mdp_sustainable_goals.course

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
import com.mdp_sustainable_goals.course.student.activity.ModuleStudentActivity
import com.mdp_sustainable_goals.course.teacher.activity.ModuleTeacherActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClassDetailActivity : AppCompatActivity() {
    private lateinit var tvCDSClass: TextView
    private lateinit var tvCDSBidang: TextView
    private lateinit var tvCDSDeskripsi: TextView
    private lateinit var btnRedirectModule: Button

    private var classId: Int = -1
    private var activityScope: String = ""
    private lateinit var classObj: ClassEntity

    private lateinit var db: AppDatabase
    private val coroutine = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_detail)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.title = "Detail Kelas"

        tvCDSClass = findViewById(R.id.tvCDSClass)
        tvCDSBidang = findViewById(R.id.tvCDSBidang)
        tvCDSDeskripsi = findViewById(R.id.tvCDSDeskripsi)
        btnRedirectModule = findViewById(R.id.btnRedirectModule)

        classId = intent.getIntExtra("class_id", -1)
        activityScope = intent.getStringExtra("activity_scope")!!

        db = AppDatabase.build(this)
        coroutine.launch {
            classObj = db.classDao().get(classId)!!
            runOnUiThread {
                tvCDSClass.text = classObj.class_nama
                tvCDSBidang.text = classObj.class_bidang_studi
                // tvCDSDeskripsi.text = classObj.class_deskripsi
            }
        }

        btnRedirectModule.setOnClickListener {
            if(activityScope == "teacher") {
                val intent = Intent(this, ModuleTeacherActivity::class.java)
                intent.putExtra("idx", classId.toString())
                startActivity(intent)
            } else {
                val intent = Intent(this, ModuleStudentActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
