package com.aedo.aedoAdmin.view.notice

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.adapter.NoticeAdapter
import com.aedo.aedoAdmin.api.APIService
import com.aedo.aedoAdmin.api.ApiUtils
import com.aedo.aedoAdmin.databinding.ActivityNoticeBinding
import com.aedo.aedoAdmin.model.notice.Announcement
import com.aedo.aedoAdmin.model.notice.NoticeModel
import com.aedo.aedoAdmin.util.base.BaseActivity
import com.aedo.aedoAdmin.util.base.MyApplication.Companion.prefs
import com.aedo.aedoAdmin.util.log.LLog
import com.aedo.aedoAdmin.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeActivity() : BaseActivity() {
    private lateinit var mBinding: ActivityNoticeBinding
    private lateinit var apiServices: APIService
    private var noticeAdapter: NoticeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notice)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        inStatusBar()
        inRecycler()
    }

    private fun inRecycler() {
        LLog.e("공지사항조회_첫번째 API")
        val verCall: Call<NoticeModel> = apiServices.getNoti(prefs.myaccesstoken)
        verCall.enqueue(object : Callback<NoticeModel> {
            override fun onResponse(call: Call<NoticeModel>, response: Response<NoticeModel>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG, "getNoti response SUCCESS -> $result")
                    setAdapter(result.result)
                } else {
                    Log.d(LLog.TAG, "getNoti response ERROR -> $result")
                    otherAPI()
                }
            }

            override fun onFailure(call: Call<NoticeModel>, t: Throwable) {
                Log.d(LLog.TAG, "getNoti Fail -> $t")
            }
        })
    }

    private fun otherAPI() {
        LLog.e("공지사항조회_두 번째 API")
        val vercall: Call<NoticeModel> = apiServices.getNoti(prefs.newaccesstoken)
        vercall.enqueue(object : Callback<NoticeModel> {
            override fun onResponse(call: Call<NoticeModel>, response: Response<NoticeModel>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG, "getNoti Second response SUCCESS -> $result")
                    setAdapter(result.result)
                } else {
                    Log.d(LLog.TAG, "getNoti Second response ERROR -> $result")
                }
            }

            override fun onFailure(call: Call<NoticeModel>, t: Throwable) {
                Log.d(LLog.TAG, "getNoti Second Fail -> $t")
            }
        })
    }

    private fun setAdapter(result: List<Announcement>?) {
        val mAdapter = result?.let {
            NoticeAdapter(it, this)
        }
        mBinding.noticeRecyclerView.adapter = mAdapter
        mBinding.noticeRecyclerView.layoutManager = LinearLayoutManager(this)
        mBinding.noticeRecyclerView.setHasFixedSize(true)
        mBinding.noticeRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        Log.d(LLog.TAG, "setAdapter success")
    }

    fun onBackClick(v: View) {
        moveMain()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        noticeAdapter?.notifyDataSetChanged()
    }
}








