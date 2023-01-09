package com.mdp_sustainable_goals.course.student.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
import com.mdp_sustainable_goals.course.local_storage.entity.JoinClassEntity
import com.mdp_sustainable_goals.course.student.adapter.RVJoinClassStudentAdapter
import com.mdp_sustainable_goals.course.student.adapter.RVListClassStudentAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentListClassFragment (
    var username : String
) : Fragment() {
    lateinit var rvListClass : RecyclerView
    lateinit var rvListClassAdapter : RVListClassStudentAdapter

    lateinit var listClass : MutableList<ClassEntity>
    lateinit var listClassTemp : MutableList<ClassEntity>
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
        return inflater.inflate(R.layout.fragment_student_list_class, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.build(requireContext())
        rvListClass = view.findViewById(R.id.rvListClassStudent)
        listJoinClass = mutableListOf()
        listClass = mutableListOf()
        listClassTemp = mutableListOf()

        coroutine.launch {
            refreshClass()
        }
        setRV()
    }
    suspend fun refreshClass() {
//        listJoinClass.clear()
//        listJoinClass.addAll(db.joinClassDao().getByUsername(username).toMutableList())
//        for(i in 0 until listJoinClass.size) {
//            var class_id = listJoinClass[i].class_id
//            var class_temp = db.classDao().get(class_id)!!
//            listClass.add(class_temp)
//        }
        listClass.clear()
        listClass.addAll(db.classDao().getAllJoined(username))
    }

    fun setRV() {
        val grid = GridLayoutManager(requireContext(),2)
        rvListClassAdapter = RVListClassStudentAdapter(listClass, R.layout.list_class_join_student, requireContext(),db,true) { id ->
            //arahkan ke classnya
            rvListClassAdapter.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Detail", Toast.LENGTH_SHORT).show()

        }
        rvListClass.adapter = rvListClassAdapter
        rvListClass.layoutManager = grid
    }

}