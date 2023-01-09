package com.mdp_sustainable_goals.course.student.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
import com.mdp_sustainable_goals.course.local_storage.entity.JoinClassEntity
import com.mdp_sustainable_goals.course.local_storage.entity.UserEntity
import com.mdp_sustainable_goals.course.student.adapter.RVJoinClassStudentAdapter
import com.mdp_sustainable_goals.course.student.adapter.RVListClassStudentAdapter
import com.mdp_sustainable_goals.course.student.fragment.StudentExploreFragment
import com.mdp_sustainable_goals.course.student.fragment.StudentListClassFragment
import com.mdp_sustainable_goals.course.teacher.adapter.ClassDashboardTeacherAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class StudentActivity : AppCompatActivity() {

    private lateinit var bottom_navigation: BottomNavigationView
    private lateinit var username : String
    private lateinit var listClass :MutableList<ClassEntity>
    lateinit var db: AppDatabase
    val coroutine = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)
        username = intent.getStringExtra("username")!!
        db = AppDatabase.build(this)
        listClass = mutableListOf()
        bottom_navigation = findViewById(R.id.bottom_navigation_student)

        var fragment = StudentExploreFragment(username)
        changeFragment(fragment)

        bottom_navigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.nav_student_explore -> {
                    var fragment = StudentExploreFragment(username)
                    changeFragment(fragment)
                }
                R.id.nav_student_class -> {
                    var fragment = StudentListClassFragment(username)
                    changeFragment(fragment)
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu,menu)
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


    private fun changeFragment(fragment: Fragment){
        val bundle = Bundle()
        fragment.arguments = bundle
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.fragment_container_student, fragment)
        fragmentManager.commit()
    }


    private fun redirectBack() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
