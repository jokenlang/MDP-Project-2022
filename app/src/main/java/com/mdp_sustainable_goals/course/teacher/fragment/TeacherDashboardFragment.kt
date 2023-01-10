package com.mdp_sustainable_goals.course.teacher.fragment

import android.app.Activity
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
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
        return inflater.inflate(R.layout.fragment_teacher_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvDashboardTeacher = view.findViewById(R.id.rvDashboardTeacher)

        val verticalLayoutManager =
            GridLayoutManager(context, 2)
        rvDashboardTeacher.layoutManager = verticalLayoutManager

        ioScope.launch {
            resetUI()
            activity?.runOnUiThread {
                rvDashboardTeacher.adapter = adapter
                rvDashboardTeacher.addItemDecoration(object :
                    DividerItemDecoration(
                        rvDashboardTeacher.context, VERTICAL
                    ) {
                    override fun onDraw(
                        c: Canvas,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        // super.onDraw(c, parent, state)
                    }
                })
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
