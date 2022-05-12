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
        mBinding.lifecycleOwner = this
        inStatusBar()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    fun onMainList(v: View) {
        moveList()
    }

    fun onMainNotice(v: View) {
        moveNotice()
    }

    fun onShopClick(v: View) {
        moveOrder()
    }

    fun onUserList(v: View) {
        moveUser()
    }

    fun onSearchClick(v: View) {

    }

}