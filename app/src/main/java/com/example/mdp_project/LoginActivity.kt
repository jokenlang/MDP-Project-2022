package com.example.mdp_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    lateinit var inUsernameLogin : EditText
    lateinit var inPasswordLogin : EditText
    lateinit var btnLogin        : Button
    lateinit var btnToRegister   : Button

    val user:ArrayList<UserEntity> = ArrayList()
    val ioScope = CoroutineScope(Dispatchers.IO)
    lateinit var db:AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inUsernameLogin = findViewById(R.id.inUserLogin)
        inPasswordLogin = findViewById(R.id.inPassLogin)
        btnLogin = findViewById(R.id.btnLogin)
        btnToRegister = findViewById(R.id.btnToRegister)

        db = Room.databaseBuilder(baseContext, AppDatabase::class.java, "a").build()
        ioScope.launch{
            val tempUser = db.userDao().getAll()
            user.clear()
            user.addAll(tempUser)
        }

        btnToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            var checkUser = false
            var index = 0
            var checkInput = checkEmail(inUsernameLogin.text.toString())
            if(checkInput == false){
                for(i in 0 until user.size){
                    if(inUsernameLogin.text.toString() == user[i].username){
                        //ketemu kembar
                        checkUser = true
                        index = i
                        break
                    }
                }
                if(checkUser == false){
//                    Toast.makeText(this@LoginActivity, "Username Belum Terdaftar", Toast.LENGTH_SHORT).show()
                    Toast(this@LoginActivity).showCustomToast("Username Belum Terdaftar",this@LoginActivity,"error");
                }
                else{
                    if(inPasswordLogin.text.toString() != user[index].password){
//                        Toast.makeText(this@LoginActivity, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                        Toast(this@LoginActivity).showCustomToast("Password tidak cocok",this@LoginActivity,"error");
                    }
                    else{
                        if (user[index].role == "teacher"){
                            val intent = Intent(this, TeacherActivity::class.java)
                            intent.putExtra("indexa", index)
                            startActivity(intent)
                        }
//                    val intent = Intent(this, MainActivity::class.java)
//                    intent.putExtra("indexa", index)
//                    startActivity(intent)
//                        Toast.makeText(this@LoginActivity, "Berhasil Login!", Toast.LENGTH_SHORT).show()
                        Toast(this@LoginActivity).showCustomToast("Berhasil Login!",this@LoginActivity,"success");
                    }
                }
            }
            else{
                for(i in 0 until user.size){
                    if(inUsernameLogin.text.toString() == user[i].email){
                        //ketemu kembar
                        checkUser = true
                        index = i
                        break
                    }
                }
                if(checkUser == false){
//                    Toast.makeText(this@LoginActivity, "Username Belum Terdaftar", Toast.LENGTH_SHORT).show()
                    Toast(this@LoginActivity).showCustomToast("Username Belum Terdaftar",this@LoginActivity,"error");
                }
                else{
                    if(inPasswordLogin.text.toString() != user[index].password){
//                        Toast.makeText(this@LoginActivity, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                        Toast(this@LoginActivity).showCustomToast("Password tidak cocok",this@LoginActivity,"success");
                    }
                    else{
                        if (user[index].role == "teacher"){
                            val intent = Intent(this, TeacherActivity::class.java)
                            intent.putExtra("indexa", index)
                            startActivity(intent)
                        }
//                    val intent = Intent(this, MainActivity::class.java)
//                    intent.putExtra("indexa", index)
//                    startActivity(intent)
//                        Toast.makeText(this@LoginActivity, "Berhasil Login!", Toast.LENGTH_SHORT).show()
                        Toast(this@LoginActivity).showCustomToast("Berhasil Login!",this@LoginActivity,"success");
                    }
                }
            }


        }
    }
    private fun checkEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}