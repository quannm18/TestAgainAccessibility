package com.example.testagainaccessibility.adapter.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testagainaccessibility.R
import com.example.testagainaccessibility.model.DataModel

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val tvRow: TextView by lazy { itemView.findViewById<TextView>(R.id.tvRow) }
    val chkRow: CheckBox by lazy { itemView.findViewById<CheckBox>(R.id.chkRow) }

    fun bind(item: DataModel) {
        tvRow.setText(item.name)
        chkRow.isChecked = item.status
    }
}