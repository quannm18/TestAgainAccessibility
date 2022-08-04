package com.example.testagainaccessibility.adapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testagainaccessibility.R
import com.example.testagainaccessibility.adapter.viewholder.MyViewHolder
import com.example.testagainaccessibility.listeners.ICheckedListener
import com.example.testagainaccessibility.model.DataModel

class MyAdapter() : RecyclerView.Adapter<MyViewHolder>() {
    var dataList: MutableList<DataModel> = mutableListOf()
    var checkList: HashSet<String> = HashSet()
    lateinit var mListener: ICheckedListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mItem = dataList[position]
        holder.bind(mItem)

        holder.itemView.setOnClickListener {
            dataList.removeAt(holder.layoutPosition)
            notifyItemRemoved(holder.layoutPosition)
        }
        if (holder.chkRow.isChecked) {
            checkList.add(mItem.packageName)
            mListener.onQualityChange(checkList)
        } else {
            checkList.remove(mItem.packageName)
            mListener.onQualityChange(checkList)
        }

        holder.chkRow.setOnClickListener {
            if (holder.chkRow.isChecked) {
                checkList.add(mItem.packageName)
            } else {
                checkList.remove(mItem.packageName)
            }
            mListener.onQualityChange(checkList)

        }

    }

    override fun getItemCount(): Int {
        return dataList.size ?: 0
    }

    fun initData(dataList: MutableList<DataModel>, mListener: ICheckedListener) {
        this.dataList = dataList
//        this.checkList.addAll(dataList)
        this.mListener = mListener
    }
}