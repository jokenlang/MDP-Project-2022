package com.mdp_sustainable_goals.course.student.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity
import com.mdp_sustainable_goals.course.local_storage.entity.QuizEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RVJoinClassStudent(
    private val listClass: List<ClassEntity>,
    private val layout: Int,
    private val context: Context?,
    private val db : AppDatabase,
    private val onItemClickListener: (id: Int) -> Unit,
) : RecyclerView.Adapter<RVJoinClassStudent.CustomViewHolder>() {
    private val coroutine = CoroutineScope(Dispatchers.IO)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        var itemView = LayoutInflater.from(parent.context)
        return CustomViewHolder(
            itemView.inflate(
                layout, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = listClass[position]
        holder.tvNama.text = item.class_nama
        holder.btnJoin.setOnClickListener {
            onItemClickListener(item.class_id!!)
        }
        coroutine.launch {
            var teacher = db.userDao().getUser(item.user_username)!!
            holder.tvTeacher.text = teacher.username
        }
    }

    override fun getItemCount(): Int {
        return listClass.size
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaClassJoinClassStudent)
        val btnJoin: Button = itemView.findViewById(R.id.btnJoinClassStudent)
        val tvTeacher: TextView = itemView.findViewById(R.id.tvNamaTeacherJoinClass)
    }
}
