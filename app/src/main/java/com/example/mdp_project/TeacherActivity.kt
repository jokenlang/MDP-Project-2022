package com.example.mdp_project

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class TeacherActivity : AppCompatActivity() {
    private lateinit var bottom_navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)
//        var index = intent.getIntExtra("indexa",0)rDao().getAll()
        var username = intent.getStringExtra("username")
        bottom_navigation = findViewById(R.id.bottom_navigation)

        val fragment: Fragment = TeacherFragment()
        val bundle = Bundle()
        bundle.putString("user", username)

        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()

        bottom_navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_teacher_dashboard -> {
                    val fragment: Fragment = TeacherFragment()

                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
                }
                R.id.nav_teacher_class -> {
                    val fragment: Fragment = ClassTeacherFragment()
                    val classFragment = ClassTeacherFragment()
                    val bundle = Bundle()
                    bundle.putString("user", username)
                    classFragment.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, classFragment).commit()
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
        when(item.itemId) {
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