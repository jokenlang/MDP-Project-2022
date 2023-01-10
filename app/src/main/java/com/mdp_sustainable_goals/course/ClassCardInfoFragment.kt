package com.mdp_sustainable_goals.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mdp_sustainable_goals.course.local_storage.entity.ClassEntity

class ClassCardInfoFragment(
    var kelas: ClassEntity
) : Fragment() {
    private lateinit var tvCDSClass: TextView
    private lateinit var tvCDSBidang: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_class_card_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvCDSClass = view.findViewById(R.id.tvCDSClass)
        tvCDSBidang = view.findViewById(R.id.tvCDSBidang)

        tvCDSClass.text = kelas.class_nama
        tvCDSBidang.text = kelas.class_bidang_studi
    }
}
