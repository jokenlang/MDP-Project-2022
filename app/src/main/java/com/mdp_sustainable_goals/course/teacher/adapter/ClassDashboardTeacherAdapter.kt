package com.mdp_sustainable_goals.course.teacher.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity

data class ClassDashboardTeacherAdapter(
    private val context: Activity,
    private val kelas: ArrayList<ClassEntity>,
    val click: (id: Int) -> Unit,
) : RecyclerView.Adapter<ClassDashboardTeacherAdapter.CustomViewHolder>() {
    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvClass: TextView = itemView.findViewById(R.id.tvListClassDashboard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        var itemView = LayoutInflater.from(parent.context)
        return CustomViewHolder(
            itemView.inflate(
                R.layout.list_class_dashboard_teacher, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = kelas[position]
        holder.tvClass.text = item.class_nama
        holder.itemView.setOnClickListener {
            item.class_id?.let { it1 -> click(it1) }
        }
    }

    override fun getItemCount(): Int {
        return kelas.size
    }
}
