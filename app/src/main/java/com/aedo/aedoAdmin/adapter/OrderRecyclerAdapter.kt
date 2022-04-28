package com.aedo.aedoAdmin.adapter

import android.annotation.SuppressLint
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
import com.aedo.aedoAdmin.model.order.GetOrder
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_CREATED
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_ITEM
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_PLACE_NAME
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_PLACE_NUMBER
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_PRICE
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_RECEIVER_NAME
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_RECEIVER_PHONE
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_SENDER_NAME
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_SENDER_PHONE
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_WORD_COMPANY
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_WORD_WORD
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_WORD_WORDSECOND
import com.aedo.aedoAdmin.view.order.OrderDetailActivity

class OrderRecyclerAdapter (private val postList: List<GetOrder>, val context: Context)
    : RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_order, parent, false)
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

        val order_flower = itemView?.findViewById<TextView>(R.id.tv_flower_pay)
        val myorder_receiver_name = itemView?.findViewById<TextView>(R.id.tv_receiver_name_detail)
        val myorder_send_name = itemView?.findViewById<TextView>(R.id.tv_send_name_detail)
        val order_timestamp = itemView?.findViewById<TextView>(R.id.tv_order_timestamp)
        val cl_order = itemView?.findViewById<ConstraintLayout>(R.id.cl_order)

        @SuppressLint("SetTextI18n")
        fun bind(itemOrder : GetOrder?, context: Context){
            order_flower?.text = "${itemOrder?.item.toString()}/${itemOrder?.price.toString()}"
            myorder_receiver_name?.text = itemOrder?.receiver?.name.toString()
            myorder_send_name?.text = itemOrder?.sender?.name.toString()
            order_timestamp?.text = itemOrder?.created.toString()

            cl_order?.setOnClickListener {
                val intent = Intent(context, OrderDetailActivity::class.java)
                intent.putExtra(MY_ORDER_PLACE_NAME, itemOrder?.place?.name.toString())
                intent.putExtra(MY_ORDER_PLACE_NUMBER, itemOrder?.place?.number.toString())
                intent.putExtra(MY_ORDER_ITEM, itemOrder?.item.toString())
                intent.putExtra(MY_ORDER_PRICE, itemOrder?.price.toString())
                intent.putExtra(MY_ORDER_RECEIVER_NAME, itemOrder?.receiver?.name.toString())
                intent.putExtra(MY_ORDER_RECEIVER_PHONE, itemOrder?.receiver?.phone.toString())
                intent.putExtra(MY_ORDER_SENDER_NAME, itemOrder?.sender?.name.toString())
                intent.putExtra(MY_ORDER_SENDER_PHONE, itemOrder?.sender?.phone.toString())
                intent.putExtra(MY_ORDER_WORD_COMPANY, itemOrder?.word?.company.toString())
                intent.putExtra(MY_ORDER_WORD_WORD, itemOrder?.word?.word.toString())
                intent.putExtra(MY_ORDER_WORD_WORDSECOND, itemOrder?.word?.wordsecond.toString())
                intent.putExtra(MY_ORDER_CREATED, itemOrder?.created.toString())
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }
    }
}