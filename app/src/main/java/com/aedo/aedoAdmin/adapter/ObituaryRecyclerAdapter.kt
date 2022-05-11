package com.aedo.aedoAdmin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aedo.aedoAdmin.model.list.Obituary
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.util.`object`.Constant.BURIED
import com.aedo.aedoAdmin.util.`object`.Constant.COFFIN_DATE
import com.aedo.aedoAdmin.util.`object`.Constant.DECEASED_NAME
import com.aedo.aedoAdmin.util.`object`.Constant.DOFP_DATE
import com.aedo.aedoAdmin.util.`object`.Constant.EOD_DATE
import com.aedo.aedoAdmin.util.`object`.Constant.LIST_IMG
import com.aedo.aedoAdmin.util.`object`.Constant.LLIST_ID
import com.aedo.aedoAdmin.util.`object`.Constant.PLACE_NAME
import com.aedo.aedoAdmin.util.`object`.Constant.RESIDENT_NAME
import com.aedo.aedoAdmin.util.`object`.Constant.TAG
import com.aedo.aedoAdmin.util.base.MyApplication.Companion.prefs
import com.aedo.aedoAdmin.view.list.ListDetailActivity

class ObituaryRecyclerAdapter(private val postList: List<Obituary>, val context: Context)
    : RecyclerView.Adapter<ObituaryRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.new_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {  //
        holder.bind(postList[position],context)
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(it,position)
        }
    }

    //데이터 개수 반환
    override fun getItemCount(): Int {
        return postList.count()
    }

    interface OnItemClickListener{
        fun onClick(v:View,position: Int)
    }

    private var itemClickListener: OnItemClickListener?=null

    inner class ViewHolder (itemView: View? ) : RecyclerView.ViewHolder(itemView!!){
        //findViewById
        val tx_date = itemView?.findViewById<TextView>(R.id.list_tx_date)    //날짜
        val tx_top_name = itemView?.findViewById<TextView>(R.id.tx_top_name) //고인이름
        val tx_body_name  = itemView?.findViewById<TextView>(R.id.tx_body_name)  //상주
        val tx_body_info =   itemView?.findViewById<TextView>(R.id.tx_body_info) //빈소
        val btn_show = itemView?.findViewById<Button>(R.id.btn_list_show)  //부고 보기

        @SuppressLint("ResourceType")
        fun bind(itemPhoto : Obituary?, context: Context){
            tx_date?.text = itemPhoto?.eod.toString()               //날짜
            tx_top_name?.text = itemPhoto?.deceased?.name.toString()
            tx_body_name?.text = itemPhoto?.resident?.name.toString()
            tx_body_info?.text=itemPhoto?.place.toString()
            prefs.myListId = itemPhoto?.id
            Log.d(TAG,"Recycler Img -> ${itemPhoto?.imgName.toString()}")

            btn_show?.setOnClickListener {
                val intent = Intent(context, ListDetailActivity::class.java)
                intent.putExtra(LLIST_ID,itemPhoto?.id.toString())
                intent.putExtra(DECEASED_NAME,itemPhoto?.deceased?.name.toString())
                intent.putExtra(LIST_IMG,itemPhoto?.imgName.toString())   //putExtra로 담는다.
                intent.putExtra(EOD_DATE,itemPhoto?.eod.toString())
                intent.putExtra(RESIDENT_NAME,itemPhoto?.resident?.name.toString())
                intent.putExtra(PLACE_NAME,itemPhoto?.place.toString())
                intent.putExtra(COFFIN_DATE,itemPhoto?.coffin.toString())
                intent.putExtra(DOFP_DATE,itemPhoto?.dofp.toString())
                intent.putExtra(BURIED,itemPhoto?.buried.toString())
                ContextCompat.startActivity(itemView.context, intent, null)
            }

        }
    }

}