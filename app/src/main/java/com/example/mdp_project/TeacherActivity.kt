package com.example.mdp_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class TeacherActivity : AppCompatActivity() {
    private lateinit var bottom_navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        bottom_navigation = findViewById(R.id.bottom_navigation)

        val fragment: Fragment = TeacherFragment()
        val bundle = Bundle()
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
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
                }
            }
            return@setOnItemSelectedListener true
        }
    }
}