package com.example.callview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.recyclerview_item.view.*


class MyAdapter(
                private val context: Context
              , firestore: FirebaseFirestore
              , uid: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var resultDTOs: ArrayList<ResultDTO> = arrayListOf()


    init {
        firestore.collection(uid).orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                resultDTOs.clear()
                Log.e("MyAdapter init", "실행")
                if (querySnapshot == null) return@addSnapshotListener

                // 데이터 받아오기
                for (snapshot in querySnapshot.documents) {
                    val item = snapshot.toObject(ResultDTO::class.java)
                    resultDTOs.add(item!!)
                }
                notifyDataSetChanged()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = (holder as CustomViewHolder).itemView
            viewHolder.textBox.text = resultDTOs[position].callNumber
    }

    override fun getItemCount(): Int {
        return resultDTOs.size
    }

}