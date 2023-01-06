package com.example.mdp_project

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClassModuleTeacherAdapter (
    private val context: Activity,
    private val modules: ArrayList<ModuleEntity>,
    val click:(id:Int)->Unit,
): RecyclerView.Adapter<ClassModuleTeacherAdapter.CustomViewHolder>() {
    inner class CustomViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvModule: TextView = itemView.findViewById(R.id.tvListModuleName)
        val tvJumlahKumpul: TextView = itemView.findViewById(R.id.tvJumlahKumpulModule)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        var itemView = LayoutInflater.from(parent.context)
        return CustomViewHolder(itemView.inflate(
            R.layout.list_module_class, parent ,false
        ))
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = modules[position]
        holder.tvModule.text = item.module_nama
        holder.itemView.setOnClickListener {
            item.module_id?.let { it1 -> click(it1) }
        }
    }

    override fun getItemCount(): Int {
        return modules.size
    }
}