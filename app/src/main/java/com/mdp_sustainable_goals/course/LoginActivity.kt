package com.mdp_sustainable_goals.course

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.room.Room
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.UserEntity
import com.mdp_sustainable_goals.course.student.activity.StudentActivity
import com.mdp_sustainable_goals.course.teacher.activity.TeacherActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    companion object {
        const val user_username = "user_username"
        const val user_email = "user_email"
        const val user_name = "user_name"
        const val user_password = "user_password"
        const val user_role = "user_role"
        const val user_saldo = "user_saldo"
    }

    lateinit var inUsernameLogin: EditText
    lateinit var inPasswordLogin: EditText
    lateinit var btnLogin: Button
    lateinit var btnToRegister: Button

    private val coroutine = CoroutineScope(Dispatchers.IO)
    val user: ArrayList<UserEntity> = ArrayList()
    val ioScope = CoroutineScope(Dispatchers.IO)
    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inUsernameLogin = findViewById(R.id.inUserLogin)
        inPasswordLogin = findViewById(R.id.inPassLogin)
        btnLogin = findViewById(R.id.btnLogin)
        btnToRegister = findViewById(R.id.btnToRegister)
        db = AppDatabase.build(this)
        db =
            Room.databaseBuilder(baseContext, AppDatabase::class.java, "a").allowMainThreadQueries()
                .build()

        coroutine.launch {
            // db.userDao().nukeTable()
            if (db.userDao().getAll() == null) {
            } else {
                val tempUser = db.userDao().getAll()
                user.clear()
                user.addAll(tempUser)
                checkSharedData()
            }
        }

        btnToRegister.setOnClickListener {
            clearAllFields()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener {
            coroutine.run {
                if (db.userDao().getAll() == null) {
                } else {
                    val tempUser = db.userDao().getAll()
                    user.clear()
                    user.addAll(tempUser)
                }
            }
            var checkUser = false
            var index = 0
            val checkInput = checkEmail(inUsernameLogin.text.toString())
            if (!checkInput) {
                for (i in 0 until user.size) {
                    if (inUsernameLogin.text.toString() == user[i].username) {
                        // ketemu kembar
                        checkUser = true
                        index = i
                        break
                    }
                }
                if (!checkUser) {
                    Toast(this@LoginActivity).showCustomToast(
                        "Username belum terdaftar",
                        this@LoginActivity,
                        "error"
                    );
                } else {
                    if (inPasswordLogin.text.toString() != user[index].password) {
                        Toast(this@LoginActivity).showCustomToast(
                            "Password tidak cocok",
                            this@LoginActivity,
                            "error"
                        );
                    } else {
                        val username = user[index].username
                        val nama = user[index].name
                        if (user[index].role == "teacher") {
                            val intent = Intent(this, TeacherActivity::class.java)
                            intent.putExtra("indexa", index)
                            intent.putExtra("username", username)
                            intent.putExtra("nama", nama)
                            startActivity(intent)
                        } else {
                            // student
                            val intent = Intent(this, StudentActivity::class.java)
                            intent.putExtra("indexa", index)
                            intent.putExtra("username", username)
                            intent.putExtra("nama", nama)
                            startActivity(intent)
                        }
                        saveSharedData(user[index])
                        clearAllFields()
                        Toast(this@LoginActivity).showCustomToast(
                            "Berhasil login!",
                            this@LoginActivity,
                            "success"
                        );
                    }
                }
            } else {
                for (i in 0 until user.size) {
                    if (inUsernameLogin.text.toString() == user[i].email) {
                        // ketemu kembar
                        checkUser = true
                        index = i
                        break
                    }
                }
                if (checkUser == false) {
                    Toast(this@LoginActivity).showCustomToast(
                        "Username belum terdaftar",
                        this@LoginActivity,
                        "error"
                    );
                } else {
                    if (inPasswordLogin.text.toString() != user[index].password) {
                        Toast(this@LoginActivity).showCustomToast(
                            "Password tidak cocok",
                            this@LoginActivity,
                            "error"
                        );
                    } else {
                        val username = user[index].username
                        val nama = user[index].name
                        if (user[index].role == "teacher") {
                            val intent = Intent(this, TeacherActivity::class.java)
                            intent.putExtra("indexa", index)
                            intent.putExtra("username", username)
                            intent.putExtra("nama", nama)
                            startActivity(intent)
                        } else {
                            // student
                            val intent = Intent(this, StudentActivity::class.java)
                            intent.putExtra("indexa", index)
                            intent.putExtra("username", username)
                            intent.putExtra("nama", nama)
                            startActivity(intent)
                        }
                        saveSharedData(user[index])
                        clearAllFields()
                        Toast(this@LoginActivity).showCustomToast(
                            "Berhasil Login!",
                            this@LoginActivity,
                            "success"
                        );
                    }
                }
            }
        }
    }

    private fun clearAllFields() {
        inUsernameLogin.setText("")
        inPasswordLogin.setText("")
    }

    private fun checkEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun saveSharedData(userEntity: UserEntity) {
        val sharedFile = packageName
        val shared: SharedPreferences = getSharedPreferences(sharedFile, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = shared.edit()
        editor.putString(user_username, userEntity.username)
        editor.putString(user_email, userEntity.email)
        editor.putString(user_name, userEntity.name)
        editor.putString(user_role, userEntity.role)
        editor.apply()
    }

    private fun checkSharedData() {
        val sharedFile = packageName
        val shared: SharedPreferences = getSharedPreferences(sharedFile, MODE_PRIVATE)
        val userRole = shared.getString(user_role, "-")
        if (userRole != "-") {
            val userUsername = shared.getString(user_username, "-")
            val userNama = shared.getString(user_name, "-")
            var checkUser = false
            var index = 0
            for(i in 0 until user.size) {
                if (userUsername == user[i].username) {
                    // ketemu kembar
                    checkUser = true
                    index = i
                    break
                }
            }
            if(userRole == "teacher") {
                val intent = Intent(this, TeacherActivity::class.java)
                intent.putExtra("indexa", index)
                intent.putExtra("username", userUsername)
                intent.putExtra("nama", userNama)
                startActivity(intent)
            } else {
                // student
                val intent = Intent(this, StudentActivity::class.java)
                intent.putExtra("indexa", index)
                intent.putExtra("username", userUsername)
                intent.putExtra("nama", userNama)
                startActivity(intent)
            }
        }
    }
}
