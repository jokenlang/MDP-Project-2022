package com.example.mdp_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    lateinit var inNamaRegis     : EditText
    lateinit var inUsernameRegis : EditText
    lateinit var inPassRegis     : EditText
    lateinit var inConfPassRegis : EditText
    lateinit var inEmailRegis    : EditText
    lateinit var btnRegister     : Button
    lateinit var btnToLogin      : Button
    lateinit var rbStudent       : RadioButton
    lateinit var rbTeacher       : RadioButton

    val username : ArrayList<String> = ArrayList()
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

        db = Room.databaseBuilder(baseContext, AppDatabase::class.java, "a").build()
        ioScope.launch {
            val tempUsername = db.userDao().getAll()
            username.clear()
            for (i in 1 until tempUsername.size)
            {
                username.add(tempUsername[i].username)
            }
        }

        rbStudent.setOnClickListener {
            Role = "student"
        }

        rbTeacher.setOnClickListener {
            Role = "teacher"
        }

        btnToLogin.setOnClickListener {
            finish()
        }

        btnRegister.setOnClickListener {
            if(inPassRegis.text.toString() != inConfPassRegis.text.toString()){
                Toast.makeText(this@RegisterActivity, "Password & Konfirmasi Password tidak sama", Toast.LENGTH_SHORT).show()
            }
            else{
                if(inNamaRegis.text.toString() == "" || inUsernameRegis.text.toString() == "" || inPassRegis.text.toString() == "" || Role == ""){
                    Toast.makeText(this@RegisterActivity, "input error", Toast.LENGTH_SHORT).show()
                }
                else{
                    var checkUser = false
                    for(i in 0 until username.size){
                        if(inUsernameRegis.text.toString() == username[i].toString()){
                            //ketemu kembar
                            checkUser = true
                            break
                        }
                    }
                    if(checkUser == true){
                        Toast.makeText(this@RegisterActivity, "Username telah terdaftar", Toast.LENGTH_SHORT).show()
                    }
                    else{
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
                            role = Role
                        )
                        ioScope.launch {
                            db.userDao().insert(user)
                        }
                        Toast.makeText(this@RegisterActivity, "Berhasil mendaftarkan user ke database", Toast.LENGTH_SHORT).show()
                        clearInput()
                    }
                }
            }
            ioScope.launch {
                val tempUsername = db.userDao().getAll()
                username.clear()
                for (i in 1 until tempUsername.size)
                {
                    username.add(tempUsername[i].username)
                }
            }
        }
    }

    fun clearInput(){
        inNamaRegis.setText("")
        inEmailRegis.setText("")
        inUsernameRegis.setText("")
        inPassRegis.setText("")
        inConfPassRegis.setText("")
    }
}