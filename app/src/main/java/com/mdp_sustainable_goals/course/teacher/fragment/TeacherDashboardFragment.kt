package com.mdp_sustainable_goals.course.teacher.fragment

import android.app.Activity
import android.content.SharedPreferences
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.LoginActivity
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
    private lateinit var textView15: TextView

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
        textView15 = view.findViewById(R.id.textView15)

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
            val sharedFile = activity?.packageName
            val shared: SharedPreferences? = activity?.getSharedPreferences(sharedFile,
                AppCompatActivity.MODE_PRIVATE
            )
            val username = shared?.getString(LoginActivity.user_username, "-")
            kelas.clear()
            kelas.addAll(db.classDao().getByPengajar(username!!))
            activity?.runOnUiThread {
                if(kelas.size > 0) {
                    rvDashboardTeacher.visibility = View.VISIBLE
                    textView15.visibility = View.GONE
                } else {
                    rvDashboardTeacher.visibility = View.GONE
                    textView15.visibility = View.VISIBLE
                }
                adapter.notifyDataSetChanged()
            }
        }
    }
}
