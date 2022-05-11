package com.aedo.aedoAdmin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.model.notice.Announcement
import com.aedo.aedoAdmin.view.notice.NoticeDetailActivity

class NoticeAdapter(private val items: List<Announcement>, val context: Context) :
    RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.view_item_notice, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], context)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val title = itemView?.findViewById<TextView>(R.id.tv_notice_item_title)
        val created = itemView?.findViewById<TextView>(R.id.tv_notice_item_timestamp)
        val cl_body = itemView?.findViewById<ConstraintLayout>(R.id.cl_notice)

        fun bind(itemUser: Announcement?, context: Context) {
            title?.text = itemUser?.title.toString()
            created?.text = itemUser?.created.toString()

            cl_body?.setOnClickListener {
                val intent = Intent(context, NoticeDetailActivity::class.java)
                intent.putExtra("title", itemUser?.title.toString())
                intent.putExtra("content", itemUser?.content.toString())
                intent.putExtra("created", itemUser?.created.toString())
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }
    }
}
