package com.aedo.aedoAdmin.view.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.api.APIService
import com.aedo.aedoAdmin.api.ApiUtils
import com.aedo.aedoAdmin.databinding.ActivityMainBinding
import com.aedo.aedoAdmin.databinding.ActivityOrderBinding
import com.aedo.aedoAdmin.util.base.BaseActivity

class OrderActivity : BaseActivity() {
    private lateinit var mBinding : ActivityOrderBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order)
        mBinding.activity = this
        apiServices = ApiUtils.apiService

    }
}