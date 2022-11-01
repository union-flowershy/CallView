package com.example.callview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.recyclerview_item.view.*


class MyAdapter(
        private val context: Context,
        private val list: ArrayList<String>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textbox: TextView = itemView.textBox
    }

    override fun getItemCount(): Int {
        Log.e(list.size.toString(), "getItemCount 작동")
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.recyclerview_item,parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val callview = list[position]
        holder.textbox.text = callview
        Log.e(callview.toString(), "onBindViewHolder 작동")
        Log.e(holder.toString(), "onBindViewHolder 작동")
    }

}