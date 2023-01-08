package com.mdp_sustainable_goals.course.student.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.mdp_sustainable_goals.course.R

class StudentActivity : AppCompatActivity() {
    lateinit var btnToJoin : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)
        var username = intent.getStringExtra("username")

        btnToJoin = findViewById(R.id.btnToJoinClass)
        btnToJoin.setOnClickListener {
            var intent = Intent(this,JoinClassStudentActivity::class.java)
            intent.putExtra("username",username)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemLogout -> {
                redirectBack()
            }
        }
        return true
    }

    private fun redirectBack() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
