package com.mdp_sustainable_goals.course.student.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
import com.mdp_sustainable_goals.course.local_storage.entity.JoinClassEntity
import com.mdp_sustainable_goals.course.ClassDetailActivity
import com.mdp_sustainable_goals.course.student.adapter.RVListClassStudentAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentListClassFragment(
    var username: String
) : Fragment() {
    lateinit var rvListClass: RecyclerView
    lateinit var rvListClassAdapter: RVListClassStudentAdapter
    lateinit var textView14: TextView

    lateinit var listClass: MutableList<ClassEntity>
    lateinit var listClassTemp: MutableList<ClassEntity>
    lateinit var listJoinClass: MutableList<JoinClassEntity>

    private lateinit var db: AppDatabase
    private val coroutine = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_list_class, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.build(requireContext())
        rvListClass = view.findViewById(R.id.rvListClassStudent)
        textView14 = view.findViewById(R.id.textView14)
        listJoinClass = mutableListOf()
        listClass = mutableListOf()
        listClassTemp = mutableListOf()
        coroutine.launch {
            refreshClass()
            activity?.runOnUiThread {
                setRV()
            }
        }
    }

    private suspend fun refreshClass() {
        listClass.clear()
        listClass.addAll(db.classDao().getAllJoined(username))
    }

    private fun setRV() {
        activity?.runOnUiThread {
            if(listClass.size > 0) {
                rvListClass.visibility = View.VISIBLE
                textView14.visibility = View.GONE
            } else {
                rvListClass.visibility = View.GONE
                textView14.visibility = View.VISIBLE
            }
        }
        val grid = GridLayoutManager(requireContext(), 2)
        rvListClassAdapter = RVListClassStudentAdapter(
            listClass,
            R.layout.list_class_join_student,
            requireContext(),
            db,
            true
        ) { id ->
            rvListClassAdapter.notifyDataSetChanged()
            val intent = Intent(requireContext(), ClassDetailActivity::class.java)
            intent.putExtra("class_id", id)
            intent.putExtra("activity_scope", "student")
            startActivity(intent)
        }
        rvListClass.adapter = rvListClassAdapter
        rvListClass.layoutManager = grid
    }
}
