package com.mdp_sustainable_goals.course.teacher.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mdp_sustainable_goals.course.ClassDetailActivity
import com.mdp_sustainable_goals.course.LoginActivity
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
import com.mdp_sustainable_goals.course.local_storage.entity.UserEntity
import com.mdp_sustainable_goals.course.teacher.fragment.ClassTeacherFragment
import com.mdp_sustainable_goals.course.teacher.fragment.TeacherDashboardFragment
import com.mdp_sustainable_goals.course.teacher.adapter.ClassDashboardTeacherAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class TeacherActivity : AppCompatActivity() {
    private lateinit var bottom_navigation: BottomNavigationView
    private lateinit var kelas: ArrayList<ClassEntity>
    private lateinit var ClassDashboardTeacherAdapter: ClassDashboardTeacherAdapter

    lateinit var db: AppDatabase
    val ioScope = CoroutineScope(Dispatchers.IO)

    private lateinit var user: UserEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        db = AppDatabase.build(this)

        // var index = intent.getIntExtra("indexa", 0)
        val username = intent.getStringExtra("username")
        val name = intent.getStringExtra("nama")
        bottom_navigation = findViewById(R.id.bottom_navigation)
        val tvDashboardTeacherName: TextView = findViewById(R.id.tvDashboardTeacherName)
        tvDashboardTeacherName.text = "$name"
        ioScope.launch {
            user = username?.let { db.userDao().getUser(it) } as UserEntity
            kelas = db.classDao().getByPengajar(user.username) as ArrayList<ClassEntity>
            ClassDashboardTeacherAdapter =
                ClassDashboardTeacherAdapter(this@TeacherActivity, kelas) { idx ->
                    val intent = Intent(this@TeacherActivity, ClassDetailActivity::class.java)
                    intent.putExtra("class_id", idx)
                    intent.putExtra("activity_scope", "teacher")
                    startActivity(intent)
                }
            val teacher_fragment = TeacherDashboardFragment(ClassDashboardTeacherAdapter)
            val bundle = Bundle()
            /* bundle.putString("user_name", user.name) */
            teacher_fragment.arguments = bundle
            val fragmentManager = supportFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.fragment_container, teacher_fragment)
            fragmentManager.commit()
        }

        bottom_navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_teacher_dashboard -> {
                    ioScope.launch {
                        user = username?.let { db.userDao().getUser(it) } as UserEntity
                        kelas = db.classDao().getByPengajar(user.username) as ArrayList<ClassEntity>
                        ClassDashboardTeacherAdapter =
                            ClassDashboardTeacherAdapter(this@TeacherActivity, kelas) { idx ->
                                val intent = Intent(this@TeacherActivity, ClassDetailActivity::class.java)
                                intent.putExtra("class_id", idx)
                                intent.putExtra("activity_scope", "teacher")
                                startActivity(intent)
                            }
                        val teacher_fragment =
                            TeacherDashboardFragment(ClassDashboardTeacherAdapter)
                        val bundle = Bundle()
                        bundle.putString("user_name", user.name)
                        teacher_fragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, teacher_fragment)
                            .setReorderingAllowed(true)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit()
                    }
                }
                R.id.nav_teacher_class -> {
                    val fragment: Fragment = ClassTeacherFragment()
                    val classFragment = ClassTeacherFragment()
                    val bundle = Bundle()
                    bundle.putString("user", username)
                    classFragment.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, classFragment)
                        .setReorderingAllowed(true)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
                }
                R.id.nav_teacher_logout -> {
                    val sharedFile = packageName
                    val shared: SharedPreferences? = getSharedPreferences(sharedFile, Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor? = shared?.edit()
                    editor?.remove("user_username")
                    editor?.remove("user_email")
                    editor?.remove("user_name")
                    editor?.remove("user_role")
                    editor?.apply()
                    finish()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
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

    private fun redirectBack() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
