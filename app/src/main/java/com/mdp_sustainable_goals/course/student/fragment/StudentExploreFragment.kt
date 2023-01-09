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

class StudentExploreFragment (
    var username : String
): Fragment() {
    lateinit var rvJoinAdapter : RVJoinClassStudentAdapter
    lateinit var rvClass : RecyclerView
    lateinit var tvDashboard : TextView

    lateinit var listAllClass : MutableList<ClassEntity>
    lateinit var listClass : MutableList<ClassEntity>
    lateinit var listJoinClass : MutableList<JoinClassEntity>
    private val coroutine = CoroutineScope(Dispatchers.IO)
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_explore, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.build(requireContext())

        listClass = mutableListOf()
        listAllClass = mutableListOf()
        listJoinClass = mutableListOf()
        rvClass = view.findViewById(R.id.rvJoinClassStudent)
        tvDashboard = view.findViewById(R.id.tvDashboardStudentName)
        coroutine.launch {
            var user = db.userDao().getUser(username)!!
            tvDashboard.text = "Welcome, ${user.username}"
            refreshClass()
        }
        setRV()
    }
    suspend fun refreshClass() {
        listAllClass.clear()
        listAllClass.addAll(db.classDao().fetch().toMutableList())
        listJoinClass.clear()
        listJoinClass.addAll(db.joinClassDao().getByUsername(username).toMutableList())
        //cari yang sama dihapus dari list class
        for(i in 0 until listJoinClass.size) {
            var temp_class = listClass.find {
                it.class_id == listJoinClass[i].class_id
            }
            listAllClass.remove(temp_class)
        }

    }

    fun setRV() {
        listClass = listAllClass
        val grid = GridLayoutManager(requireContext(),2)
        rvJoinAdapter = RVJoinClassStudentAdapter(listClass, R.layout.list_class_join_student, requireContext(),db) { id ->
            coroutine.launch {
                var jc = JoinClassEntity(null,username,id)
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