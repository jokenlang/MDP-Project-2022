package com.mdp_sustainable_goals.course.student.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
import com.mdp_sustainable_goals.course.student.fragment.StudentExploreFragment
import com.mdp_sustainable_goals.course.student.fragment.StudentListClassFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class StudentActivity : AppCompatActivity() {
    private lateinit var bottom_navigation: BottomNavigationView
    private lateinit var username: String
    private lateinit var listClass: MutableList<ClassEntity>
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
            when (it.itemId) {
                R.id.nav_student_explore -> {
                    var fragment = StudentExploreFragment(username)
                    changeFragment(fragment)
                }
                R.id.nav_student_class -> {
                    var fragment = StudentListClassFragment(username)
                    changeFragment(fragment)
                }
                R.id.nav_student_logout -> {
                    val sharedFile = packageName
                    val shared: SharedPreferences? =
                        getSharedPreferences(sharedFile, Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor? = shared?.edit()
                    editor?.remove("user_username")
                    editor?.remove("user_email")
                    editor?.remove("user_name")
                    editor?.remove("user_role")
                    editor?.apply()
                    finish()
                }
            }
            return@setOnItemSelectedListener true
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

    private fun changeFragment(fragment: Fragment) {
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
