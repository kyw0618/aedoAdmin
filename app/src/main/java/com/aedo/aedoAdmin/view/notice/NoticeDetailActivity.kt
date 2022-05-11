package com.aedo.aedoAdmin.view.notice

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.databinding.ActivityNoticeBinding
import com.aedo.aedoAdmin.databinding.ActivityNoticeDetailBinding
import com.aedo.aedoAdmin.model.notice.Announcement
import com.aedo.aedoAdmin.util.base.BaseActivity
import com.aedo.aedoAdmin.util.log.LLog

class NoticeDetailActivity : BaseActivity() {

    private lateinit var mBinding: ActivityNoticeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notice_detail)
        mBinding.activity = this
        inStatusBar()
        initView()
    }

    private fun initView() {
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val created = intent.getStringExtra("created")

        mBinding.tvTitle.text = title.toString()
        mBinding.tvContent.text = content.toString()
        mBinding.tvCreated.text = created.toString()
    }

    fun onBackClick(v: View) {
        moveMain()
    }
}
