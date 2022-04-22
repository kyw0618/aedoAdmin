package com.aedo.aedoAdmin.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.api.APIService
import com.aedo.aedoAdmin.api.ApiUtils
import com.aedo.aedoAdmin.databinding.ActivityMainBinding
import com.aedo.aedoAdmin.util.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var mBinding : ActivityMainBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
    }

    fun onMakeClick(v: View) {

    }

    fun onSendClick(v: View) {

    }

    fun onCenterClick(v: View) {

    }

    fun onSearchClick(v: View) {

    }

    fun onShopClick(v: View) {

    }

    fun onSetting(v: View) {

    }

    fun onNoticeCLick(v: View) {

    }
}