package com.mdp_sustainable_goals.course.teacher.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
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
        val scrollView: ScrollView = itemView.findViewById(R.id.scrollView)
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = modules[position]
        holder.scrollView.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
        holder.tvListModuleName.text = "Module ${item.module_nama}"
        holder.tvListModuleDeskripsi.text = item.module_deskripsi
        // holder.tvListModuleDeskripsi.movementMethod = ScrollingMovementMethod()
        holder.tvJumlahKumpul.text = "Jumlah Terkumpul: 0/20"
        holder.itemView.setOnClickListener {
            item.module_id?.let { it1 -> click(it1) }
        }
    }

    override fun getItemCount(): Int {
        return modules.size
    }
}
