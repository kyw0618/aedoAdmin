package com.aedo.aedoAdmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.model.login.GetUser

class UserRecyclerAdapter (private val postList: List<GetUser>, val context: Context)
    : RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postList[position],context)
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(it,position)
        }
    }

    interface OnItemClickListener{
        fun onClick(v: View, position: Int)
    }

    private var itemClickListener: OnItemClickListener?=null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    override fun getItemCount(): Int {
        return postList.count()
    }


    inner class ViewHolder (itemView: View? ) : RecyclerView.ViewHolder(itemView!!){

        val user_name = itemView?.findViewById<TextView>(R.id.tv_user_name)
        val user_phone = itemView?.findViewById<TextView>(R.id.tv_user_phone)
        val user_birth = itemView?.findViewById<TextView>(R.id.tv_user_birth)

        fun bind(itemUser : GetUser?, context: Context){
            user_name?.text = itemUser?.name.toString()
            user_birth?.text = itemUser?.birth.toString()
            user_phone?.text = itemUser?.phone.toString()
        }
    }
}