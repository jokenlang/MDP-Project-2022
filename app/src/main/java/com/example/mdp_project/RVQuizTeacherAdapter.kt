package com.example.mdp_project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RVQuizTeacherAdapter(
    private val listQuiz : List<QuizEntity>,
    private val layout: Int,
    private val context: Context?,
    private val onItemClickListener: (id:Int, mode:String)->Unit,
): RecyclerView.Adapter<RVQuizTeacherAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        var itemView = LayoutInflater.from(parent.context)
        return CustomViewHolder(
            itemView.inflate(
                layout, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = listQuiz[position]
        holder.tvNama.text = item.quiz_nama
        holder.btnDetail.setOnClickListener {
            onItemClickListener(item.quiz_id!!,"detail")
        }


    }


    override fun getItemCount(): Int {
        return listQuiz.size
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaListQuizRV)
        val btnDetail : Button = itemView.findViewById(R.id.btnEditListQuizRV)

    }
}