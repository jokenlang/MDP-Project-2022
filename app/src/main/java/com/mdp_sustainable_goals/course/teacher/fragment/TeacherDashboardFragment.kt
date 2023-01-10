package com.mdp_sustainable_goals.course.teacher.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
import com.mdp_sustainable_goals.course.teacher.adapter.ClassDashboardTeacherAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeacherDashboardFragment(
    var adapter: ClassDashboardTeacherAdapter
) : Fragment() {
    var kelas: ArrayList<ClassEntity> = arrayListOf()
    private lateinit var db: AppDatabase
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private lateinit var rvDashboardTeacher: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = AppDatabase.build(context as Activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvDashboardTeacher = view.findViewById(R.id.rvDashboardTeacher)

        var user_name = arguments?.getString("user_name")
        var tvDashboardTeacherName: TextView = view.findViewById(R.id.tvDashboardTeacherName)
        tvDashboardTeacherName.setText(" $user_name")
        val verticalLayoutManager =
            GridLayoutManager(context, 3)
        rvDashboardTeacher.layoutManager = verticalLayoutManager

        ioScope.launch {
            resetUI()
            activity?.runOnUiThread {
                rvDashboardTeacher.adapter = adapter
                rvDashboardTeacher.addItemDecoration(
                    DividerItemDecoration(
                        rvDashboardTeacher.context,
                        DividerItemDecoration.VERTICAL
                    )
                )
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun resetUI() {
        ioScope.launch {
            kelas.clear()
            kelas.addAll(db.classDao().getAll())
            activity?.runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }
}
