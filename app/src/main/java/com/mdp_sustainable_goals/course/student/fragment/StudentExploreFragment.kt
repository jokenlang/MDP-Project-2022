package com.mdp_sustainable_goals.course.student.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
import com.mdp_sustainable_goals.course.local_storage.entity.JoinClassEntity
import com.mdp_sustainable_goals.course.student.adapter.RVJoinClassStudentAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentExploreFragment(
    var username: String
) : Fragment() {
    lateinit var rvJoinAdapter: RVJoinClassStudentAdapter
    lateinit var rvClass: RecyclerView

    lateinit var listAllClass: MutableList<ClassEntity>
    lateinit var listClass: MutableList<ClassEntity>
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
        return inflater.inflate(R.layout.fragment_student_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.build(requireContext())
        listClass = mutableListOf()
        listAllClass = mutableListOf()
        listJoinClass = mutableListOf()
        rvClass = view.findViewById(R.id.rvJoinClassStudent)
        coroutine.launch {
            refreshClass()
            activity?.runOnUiThread {
                setRV()
            }
        }
    }

    private suspend fun refreshClass() {
        listAllClass.clear()
        listAllClass.addAll(db.classDao().getAllNotJoined(username))
    }

    private fun setRV() {
        listClass = listAllClass
        val grid = GridLayoutManager(requireContext(), 2)
        rvJoinAdapter = RVJoinClassStudentAdapter(
            listClass,
            R.layout.list_class_join_student,
            requireContext(),
            db
        ) { id ->
            coroutine.launch {
                var jc = JoinClassEntity(null, username, id)
                db.joinClassDao().insert(jc)
                changeFragmentToListClass()
            }
        }
        rvClass.adapter = rvJoinAdapter
        rvClass.layoutManager = grid
    }

    fun changeFragmentToListClass() {
        val fragment = StudentListClassFragment(username)
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_student, fragment)
            .commit()
    }
}
