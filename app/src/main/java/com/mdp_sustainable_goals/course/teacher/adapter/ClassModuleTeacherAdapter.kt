package com.mdp_sustainable_goals.course.teacher.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.entity.ModuleEntity

class ClassModuleTeacherAdapter(
    private val context: Activity,
    private val modules: ArrayList<ModuleEntity>,
    val click: (id: Int) -> Unit,
) : RecyclerView.Adapter<ClassModuleTeacherAdapter.CustomViewHolder>() {
    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvModule: TextView = itemView.findViewById(R.id.tvListModuleName)
        val tvJumlahKumpul: TextView = itemView.findViewById(R.id.tvJumlahKumpulModule)
        val bg: ConstraintLayout = itemView.findViewById(R.id.clListModule)
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
        holder.tvModule.text = item.module_nama
        holder.itemView.setOnClickListener {
            item.module_id?.let { it1 -> click(it1) }
        }
        holder.bg.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner))
        /*holder.bg.setBackgroundColor(context.getResources().getColor(R.color.module_blue))*/
    }

    override fun getItemCount(): Int {
        return modules.size
    }
}
