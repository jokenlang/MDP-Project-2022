package com.example.mdp_project

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeacherFragment : Fragment() {

    var kelas: ArrayList<ClassEntity> = arrayListOf()
    private lateinit var db: AppDatabase
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private lateinit var rvDashboardTeacher: RecyclerView
    private lateinit var classAdapter: ClassDashboardTeacherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = AppDatabase.build(context as Activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvDashboardTeacher = view.findViewById(R.id.rvDashboardTeacher)

        val verticalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        rvDashboardTeacher.layoutManager = verticalLayoutManager

        ioScope.launch {
            resetUI()

            activity?.runOnUiThread {
                classAdapter = ClassDashboardTeacherAdapter(context as Activity, R.layout.list_class_dashboard_teacher, kelas)
                rvDashboardTeacher.adapter = classAdapter
                rvDashboardTeacher.addItemDecoration(DividerItemDecoration(rvDashboardTeacher.context, DividerItemDecoration.VERTICAL))
                classAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun resetUI() {
        ioScope.launch {
            kelas.clear()
            kelas.addAll(db.classDao().getAll())

            activity?.runOnUiThread {
                classAdapter.notifyDataSetChanged()
            }
        }
    }
}