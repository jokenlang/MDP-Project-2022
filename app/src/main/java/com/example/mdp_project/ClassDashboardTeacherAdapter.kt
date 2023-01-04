package com.example.mdp_project

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class ClassDashboardTeacherAdapter (
    private val context: Activity,
    private val layout: Int,
    private val kelas: ArrayList<ClassEntity>
): RecyclerView.Adapter<ClassDashboardTeacherAdapter.CustomViewHolder>() {
    inner class CustomViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvClass: TextView = itemView.findViewById(R.id.tvListClassDashboard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = kelas[position]
        print(item.class_nama)
        holder.tvClass.text = item.class_nama
    }

    override fun getItemCount(): Int {
        return kelas.size
    }
}