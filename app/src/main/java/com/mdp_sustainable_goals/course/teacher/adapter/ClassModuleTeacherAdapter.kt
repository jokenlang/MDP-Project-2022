package com.mdp_sustainable_goals.course.teacher.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.entity.ModuleEntity

class ClassModuleTeacherAdapter(
    private val context: Activity,
    private val modules: ArrayList<ModuleEntity>,
    val click: (id: Int) -> Unit,
) : RecyclerView.Adapter<ClassModuleTeacherAdapter.CustomViewHolder>() {
    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvListModuleName: TextView = itemView.findViewById(R.id.tvListModuleName)
        val tvListModuleDeskripsi: TextView = itemView.findViewById(R.id.tvListModuleDeskripsi)
        val tvJumlahKumpul: TextView = itemView.findViewById(R.id.tvJumlahKumpulModule)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        return CustomViewHolder(
            itemView.inflate(
                R.layout.list_module_class, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = modules[position]
        holder.tvListModuleName.text = "Module ${item.module_nama}"
        holder.tvListModuleDeskripsi.text = item.module_deskripsi
        holder.tvJumlahKumpul.text = "Jumlah Terkumpul: 0/20"
        holder.itemView.setOnClickListener {
            item.module_id?.let { it1 -> click(it1) }
        }
    }

    override fun getItemCount(): Int {
        return modules.size
    }
}
