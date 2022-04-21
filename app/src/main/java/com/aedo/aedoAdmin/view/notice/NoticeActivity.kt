package com.aedo.aedoAdmin.view.notice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.api.APIService
import com.aedo.aedoAdmin.api.ApiUtils
import com.aedo.aedoAdmin.databinding.ActivityMainBinding
import com.aedo.aedoAdmin.databinding.ActivityNoticeBinding
import com.aedo.aedoAdmin.util.base.BaseActivity

class NoticeActivity : BaseActivity() {
    private lateinit var mBinding : ActivityNoticeBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notice)
        mBinding.activity = this
        apiServices = ApiUtils.apiService

    }
}