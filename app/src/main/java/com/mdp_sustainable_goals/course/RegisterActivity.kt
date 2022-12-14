package com.mdp_sustainable_goals.course

import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    lateinit var inNamaRegis: EditText
    lateinit var inUsernameRegis: EditText
    lateinit var inPassRegis: EditText
    lateinit var inConfPassRegis: EditText
    lateinit var inEmailRegis: EditText
    lateinit var btnRegister: Button
    lateinit var btnToLogin: Button
    lateinit var rbStudent: RadioButton
    lateinit var rbTeacher: RadioButton
    lateinit var rgRole: RadioGroup
    lateinit var listUser : MutableList<UserEntity>

    val username: ArrayList<String> = ArrayList()
    val email: ArrayList<String> = ArrayList()
    var Role = ""
    private lateinit var db: AppDatabase
    val ioScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        inNamaRegis = findViewById(R.id.inNamaRegis)
        inUsernameRegis = findViewById(R.id.inUsernameRegis)
        inPassRegis = findViewById(R.id.inPassRegisPassword)
        inConfPassRegis = findViewById(R.id.inConfPassRegis)
        inEmailRegis = findViewById(R.id.inEmailRegister)
        btnRegister = findViewById(R.id.btnRegister)
        btnToLogin = findViewById(R.id.btnToLogin)
        rbStudent = findViewById(R.id.rbStudent)
        rbTeacher = findViewById(R.id.rbTeacher)
        rgRole = findViewById(R.id.rgRole)

        db = Room.databaseBuilder(baseContext, AppDatabase::class.java, "a").build()
        listUser = mutableListOf()
        ioScope.launch {
            listUser.clear()
            listUser.addAll(db.userDao().getAll().toMutableList())
            if (db.userDao().getAll() == null) {
            } else {
                val tempUsername = db.userDao().getAll()
                username.clear()
                for (i in 0 until tempUsername.size) {
                    username.add(tempUsername[i].username)
                }
            }
        }

        rbStudent.setOnClickListener {
            Role = "student"
        }
        rbTeacher.setOnClickListener {
            Role = "teacher"
        }
        btnToLogin.setOnClickListener {
            clearAllFields()
            finish()
        }

        btnRegister.setOnClickListener {
            if (inPassRegis.text.toString() != inConfPassRegis.text.toString()) {
                Toast(this@RegisterActivity).showCustomToast(
                    "Password tidak sama",
                    this@RegisterActivity,
                    "error"
                );
            } else {
                if (checkEmail(inEmailRegis.text.toString()) == false) {
                    Toast(this@RegisterActivity).showCustomToast(
                        "Email tidak valid",
                        this@RegisterActivity,
                        "error"
                    );
                } else if (inNamaRegis.text.toString() == "" || inUsernameRegis.text.toString() == "" || inPassRegis.text.toString() == "" || Role == "") {
                    Toast(this@RegisterActivity).showCustomToast(
                        "Input Error",
                        this@RegisterActivity,
                        "error"
                    );
                } else {
                    var checkUser = false
//                    for (i in 0 until username.size) {
//                        if (inUsernameRegis.text.toString() == username[i]) {
//                            // ketemu kembar
//                            checkUser = true
//                            break
//                        }
//                    }
                    for (i in 0 until listUser.size) {
                        if(inUsernameRegis.text.toString() == listUser[i].username || inEmailRegis.text.toString() == listUser[i].email) {
                            checkUser = true
                            break
                        }
                    }

                    if (checkUser == true) {
                        Toast(this@RegisterActivity).showCustomToast(
                            "Username / Email telah terdaftar",
                            this@RegisterActivity,
                            "error"
                        );
                    } else {
                        val name = inNamaRegis.text.toString()
                        val username = inUsernameRegis.text.toString()
                        val password = inPassRegis.text.toString()
                        val email = inEmailRegis.text.toString()
                        val role = Role
                        val user = UserEntity(
                            username = username,
                            email = email,
                            name = name,
                            password = password,
                            role = Role,
                            saldo = 0,
                            seed = System.currentTimeMillis().toString(),
                        )
                        ioScope.launch {
                            db.userDao().insert(user)
                        }
                        Toast(this@RegisterActivity).showCustomToast(
                            "Berhasil mendaftarkan user",
                            this@RegisterActivity,
                            "success"
                        );
                        clearAllFields()
                        finish()
                    }
                }
            }
            ioScope.launch {
                listUser.clear()
                listUser.addAll(db.userDao().getAll().toMutableList())
                val tempUsername = db.userDao().getAll()
                username.clear()
                for (i in 0 until tempUsername.size) {
                    username.add(tempUsername[i].username)
                }
            }
        }
    }

    private fun clearAllFields() {
        inNamaRegis.setText("")
        inEmailRegis.setText("")
        inUsernameRegis.setText("")
        inPassRegis.setText("")
        inConfPassRegis.setText("")
        rgRole.clearCheck()
    }

    private fun checkEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
