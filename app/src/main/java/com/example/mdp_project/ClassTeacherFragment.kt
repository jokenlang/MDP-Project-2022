package com.example.mdp_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClassTeacherFragment() : Fragment() {
    private lateinit var db: AppDatabase
    val ioScope = CoroutineScope(Dispatchers.IO)
    private val coroutine = CoroutineScope(Dispatchers.IO)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_teacher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // cara panggil di fragment
//        Toast(requireContext()).showCustomToast("Hello",requireActivity(),"success")
        val btnTambahKelas : Button = view.findViewById(R.id.btnTambahKelas)
        val etNamaKelas          : EditText = view.findViewById(R.id.etNamaKelas)
        val etBidangStudiKelas   : EditText = view.findViewById(R.id.etBidangStudiKelas)
        

        btnTambahKelas.setOnClickListener {
            if(etNamaKelas.text.toString() == "" || etBidangStudiKelas.text.toString() == ""){
            }
            else{
                var Username = arguments?.getString("user")
                val Class = ClassEntity(
                    class_id    = null,
                    class_nama         = etNamaKelas.text.toString(),
                    user_username      = Username.toString(),
                    class_bidang_studi = etBidangStudiKelas.text.toString(),
                    class_status       = 1,
                )
                coroutine.launch {
                    db.classDao().insert(Class)
                }
            }
        }
    }
}