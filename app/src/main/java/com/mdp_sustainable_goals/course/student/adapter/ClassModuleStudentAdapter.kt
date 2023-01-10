package com.mdp_sustainable_goals.course.student.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.mdp_sustainable_goals.course.LoginActivity
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.ModuleEntity
import com.mdp_sustainable_goals.course.local_storage.entity.SubmissionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClassModuleStudentAdapter(
    private val context: Activity,
    private val modules: ArrayList<ModuleEntity>,
    val click: (id: Int) -> Unit,
) : RecyclerView.Adapter<ClassModuleStudentAdapter.CustomViewHolder>() {
    lateinit var db: AppDatabase
    val coroutine = CoroutineScope(Dispatchers.IO)
    var tempSubmission: SubmissionEntity? = null

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val scrollView: ScrollView = itemView.findViewById(R.id.scrollView)
        val tvListModuleName: TextView = itemView.findViewById(R.id.tvListModuleName)
        val tvListModuleDeskripsi: TextView = itemView.findViewById(R.id.tvListModuleDeskripsi)
        val tvJumlahKumpulModule: TextView = itemView.findViewById(R.id.tvJumlahKumpulModule)
        val tvDateKumpul: TextView = itemView.findViewById(R.id.tvDateKumpul)
        val tvNilaiModule: TextView = itemView.findViewById(R.id.tvNilaiModule)
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
    override fun onBindViewHolder(holder: ClassModuleStudentAdapter.CustomViewHolder, position: Int) {
        val sharedFile = context.packageName
        val shared: SharedPreferences = context.getSharedPreferences(sharedFile,
            AppCompatActivity.MODE_PRIVATE
        )
        val userRole = shared.getString(LoginActivity.user_role, "-")
        if(userRole == "teacher") {
            holder.tvJumlahKumpulModule.visibility = View.VISIBLE
            holder.tvDateKumpul.visibility = View.GONE
            holder.tvNilaiModule.visibility = View.GONE
        } else {
            holder.tvJumlahKumpulModule.visibility = View.GONE
            holder.tvDateKumpul.visibility = View.VISIBLE
            holder.tvNilaiModule.visibility = View.VISIBLE
        }
        val item = modules[position]
        holder.scrollView.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
        holder.tvListModuleName.text = "Module ${item.module_nama}"
        holder.tvListModuleDeskripsi.text = item.module_deskripsi
        // holder.tvListModuleDeskripsi.movementMethod = ScrollingMovementMethod()
        holder.tvJumlahKumpulModule.text = "Jumlah Terkumpul: 0/20"
        holder.itemView.setOnClickListener {
            item.module_id?.let { it1 -> click(it1) }
        }
        db = AppDatabase.build(context)
        coroutine.launch {
            tempSubmission = db.submissionDao().getByModuleId(item.module_id!!)
            context.runOnUiThread {
                if(tempSubmission == null) {
                    holder.tvDateKumpul.text = "Dikumpul Pada: -"
                    holder.tvNilaiModule.text = "Nilai Anda: -"
                } else {
                    holder.tvDateKumpul.text = "Dikumpul Pada: ${tempSubmission!!.submission_date}"
                    holder.tvNilaiModule.text = "Nilai Anda: ${tempSubmission!!.submission_score}"
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return modules.size
    }
}
