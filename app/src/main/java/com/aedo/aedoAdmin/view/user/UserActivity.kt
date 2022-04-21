package com.aedo.aedoAdmin.view.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.api.APIService
import com.aedo.aedoAdmin.api.ApiUtils
import com.aedo.aedoAdmin.databinding.ActivityMainBinding
import com.aedo.aedoAdmin.databinding.ActivityUserBinding
import com.aedo.aedoAdmin.util.base.BaseActivity

class UserActivity : BaseActivity() {
    private lateinit var mBinding : ActivityUserBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user)
        mBinding.activity = this
        apiServices = ApiUtils.apiService

    }
}