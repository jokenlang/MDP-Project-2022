package com.mdp_sustainable_goals.course.teacher.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.room.Room
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.dao.ClassDao
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_teacher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // cara panggil di fragment
//        Toast(requireContext()).showCustomToast("Hello",requireActivity(),"success")
        val btnTambahKelas: Button = view.findViewById(R.id.btnTambahKelas)
        val etNamaKelas: EditText = view.findViewById(R.id.etNamaKelas)
        val etBidangStudiKelas: EditText = view.findViewById(R.id.etBidangStudiKelas)
        var namakelas = etNamaKelas.text.toString()
        var bidangstudi = etBidangStudiKelas.text.toString()
        var Username = arguments?.getString("user")

        db = activity?.let { Room.databaseBuilder(it, AppDatabase::class.java, "a").build() }



        btnTambahKelas.setOnClickListener {
            if (etNamaKelas.text.toString() == "" || etBidangStudiKelas.text.toString() == "") {
            } else {
                println(
                    etNamaKelas.text.toString() + "-" + etBidangStudiKelas.text.toString() + "-" + arguments?.getString(
                        "user"
                    )
                )

                val Class = ClassEntity(
                    class_id = null,
                    class_nama = etNamaKelas.text.toString(),
                    user_username = arguments?.getString("user").toString(),
                    class_bidang_studi = etBidangStudiKelas.text.toString(),
                    class_status = 1,
                )
                coroutine.launch {
                    db?.classDao()?.insert(Class)
                }
            }
        }
    }

    private fun insertClass(namaKelas: String, bidangStudi: String) {
        var Username = arguments?.getString("user")
        val Class = ClassEntity(
            class_id = null,
            class_nama = namaKelas,
            user_username = Username.toString(),
            class_bidang_studi = bidangStudi,
            class_status = 1,
        )
        coroutine.launch {
            db?.classDao()?.insert(Class)
        }
    }
}
