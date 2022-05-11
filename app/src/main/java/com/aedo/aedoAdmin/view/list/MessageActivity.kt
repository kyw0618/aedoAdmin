package com.aedo.aedoAdmin.view.list

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.adapter.MessageRecyclerAdapter
import com.aedo.aedoAdmin.api.APIService
import com.aedo.aedoAdmin.api.ApiUtils
import com.aedo.aedoAdmin.databinding.ActivityMessageBinding
import com.aedo.aedoAdmin.model.list.Condole
import com.aedo.aedoAdmin.model.list.CondoleList
import com.aedo.aedoAdmin.util.`object`.Constant
import com.aedo.aedoAdmin.util.base.BaseActivity
import com.aedo.aedoAdmin.util.base.MyApplication.Companion.prefs
import com.aedo.aedoAdmin.util.log.LLog
import com.aedo.aedoAdmin.util.log.LLog.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MessageActivity : BaseActivity() {

    private lateinit var mBinding : ActivityMessageBinding //mBinding
    private lateinit var apiServices: APIService
    private var messageRead: MessageRecyclerAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_message)
        mBinding.activity = this@MessageActivity
        apiServices = ApiUtils.apiService
        inStatusBar()
        inRecycler()
    }

    private fun inRecycler() {
        LLog.e("조문메세지 조회_첫번째 API")
        val listId = intent.getStringExtra(Constant.MESSAGE_LLIST_ID)
        val vercall: Call<Condole> = apiServices.getConID(listId, prefs.myaccesstoken)
        vercall.enqueue(object : Callback<Condole> {
            override fun onResponse(call: Call<Condole>, response: Response<Condole>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"Condole response SUCCESS -> $result")
                    setAdapter(result.condoleMessage!!)
                }
                else {
                    Log.d(TAG,"Condole response ERROR -> $result")
                    otherAPI()
                }
            }
            override fun onFailure(call: Call<Condole>, t: Throwable) {
                Log.d(TAG, "Condole FAIL -> $t")
                Log.d(TAG,"parms ->${prefs.myListId}")
            }
        })
    }

    private fun otherAPI() {
        LLog.e("조문메세지 조회_두번째 API")
        val listId = intent.getStringExtra(Constant.MESSAGE_LLIST_ID)
        val vercall: Call<Condole> = apiServices.getConID(listId, prefs.newaccesstoken)
        vercall.enqueue(object : Callback<Condole> {
            override fun onResponse(call: Call<Condole>, response: Response<Condole>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"Condole second response SUCCESS -> $result")
                    setAdapter(result.condoleMessage!!)
                }
                else {
                    Log.d(TAG,"Condole second response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<Condole>, t: Throwable) {
                Log.d(TAG, "Condole second FAIL -> $t")
            }
        })
    }

    private fun setAdapter(list: List<CondoleList>) {
        val adapter = MessageRecyclerAdapter(list,this)
        val rv = findViewById<View>(R.id.rc_message) as RecyclerView
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        rv.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
    }


    fun onBackClick(v: View) {
        moveList()
    }

    fun onMainClick(v: View) {
        moveMain()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, ListActivity::class.java))
        finish()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        messageRead?.notifyDataSetChanged()
    }
}