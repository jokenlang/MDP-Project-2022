package com.mdp_sustainable_goals.course.teacher.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.dao.ClassDao
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
import com.mdp_sustainable_goals.course.showCustomToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClassTeacherFragment() : Fragment() {
    private var db: AppDatabase? = null
    private val coroutine = CoroutineScope(Dispatchers.IO)
    private lateinit var dao: ClassDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_class_teacher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnTambahKelas: Button = view.findViewById(R.id.btnTambahKelas)
        val etNamaKelas: EditText = view.findViewById(R.id.etNamaKelas)
        val etBidangStudiKelas: EditText = view.findViewById(R.id.etBidangStudiKelas)
        val etDeskripsiKelas: EditText = view.findViewById(R.id.etDeskripsiKelas)
        var namakelas = etNamaKelas.text.toString()
        var bidangstudi = etBidangStudiKelas.text.toString()
        var username = arguments?.getString("username")

        db = activity?.let { Room.databaseBuilder(it, AppDatabase::class.java, "a").build() }

        btnTambahKelas.setOnClickListener {
            if (etNamaKelas.text.toString() == "" || etBidangStudiKelas.text.toString() == "" || etDeskripsiKelas.text.toString() == "") {
                Toast(requireContext()).showCustomToast("Input Error", requireActivity(), "error")
            } else {
                println(
                    etNamaKelas.text.toString() + "-" + etBidangStudiKelas.text.toString() + "-" + etDeskripsiKelas.text.toString() + "-" + arguments?.getString(
                        "user"
                    )
                )
                val Class = ClassEntity(
                    class_id = null,
                    class_nama = etNamaKelas.text.toString(),
                    user_username = arguments?.getString("user").toString(),
                    class_bidang_studi = etBidangStudiKelas.text.toString(),
                    class_deskripsi = etDeskripsiKelas.text.toString(),
                    class_status = 1,
                )
                coroutine.launch {
                    db?.classDao()?.insert(Class)
                }
                etNamaKelas.setText("")
                etBidangStudiKelas.setText("")
                etDeskripsiKelas.setText("")
                Toast(requireContext()).showCustomToast(
                    "Kelas berhasil ditambah",
                    requireActivity(),
                    "success"
                )
            }
        }
    }
}
