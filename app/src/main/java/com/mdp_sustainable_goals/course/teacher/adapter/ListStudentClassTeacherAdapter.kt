package com.mdp_sustainable_goals.course.teacher.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
import com.mdp_sustainable_goals.course.local_storage.entity.UserEntity

data class ListStudentClassTeacherAdapter(
    private val context: Activity,
    private val list_student: ArrayList<UserEntity >,
) : RecyclerView.Adapter<ListStudentClassTeacherAdapter.CustomViewHolder>() {
    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvClass: TextView = itemView.findViewById(R.id.tvNamaMahasiswaClassTeacher)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        var itemView = LayoutInflater.from(parent.context)
        return CustomViewHolder(
            itemView.inflate(
                R.layout.list_student_class_teacher, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = list_student[position]
        holder.tvClass.text = item.name
    }

    override fun getItemCount(): Int {
        return list_student.size
    }
}