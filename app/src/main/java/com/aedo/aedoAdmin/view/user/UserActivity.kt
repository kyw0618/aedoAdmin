package com.aedo.aedoAdmin.view.user

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.adapter.UserRecyclerAdapter
import com.aedo.aedoAdmin.api.APIService
import com.aedo.aedoAdmin.api.ApiUtils
import com.aedo.aedoAdmin.databinding.ActivityMainBinding
import com.aedo.aedoAdmin.databinding.ActivityUserBinding
import com.aedo.aedoAdmin.model.login.GetUser
import com.aedo.aedoAdmin.model.login.UserModel
import com.aedo.aedoAdmin.util.base.BaseActivity
import com.aedo.aedoAdmin.util.base.MyApplication.Companion.prefs
import com.aedo.aedoAdmin.util.log.LLog
import com.aedo.aedoAdmin.util.log.LLog.TAG
import com.aedo.aedoAdmin.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivity : BaseActivity() {
    private lateinit var mBinding : ActivityUserBinding
    private lateinit var apiServices: APIService
    private var useradapter: UserRecyclerAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        inStatusBar()
        inRecycler()
    }

    private fun inRecycler() {
        LLog.e("유저조회_첫번째 API")
        val vercall: Call<UserModel> = apiServices.getUser(prefs.myaccesstoken)
        vercall.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"getUser response SUCCESS -> $result")
                    setAdapter(result.getuser)
                }
                else {
                    Log.d(TAG,"getUser response ERROR -> $result")
                    otherAPI()
                }
            }
            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.d(TAG, "getUser Fail -> $t")
            }
        })
    }

    private fun otherAPI() {
        LLog.e("유저조회_두번째 API")
        val vercall: Call<UserModel> = apiServices.getUser(prefs.newaccesstoken)
        vercall.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"getUser Second response SUCCESS -> $result")
                    setAdapter(result.getuser)
                }
                else {
                    Log.d(TAG,"getUser Second response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.d(TAG, "getUser Second Fail -> $t")
            }
        })
    }

    private fun setAdapter(getuser: List<GetUser>?) {
        val mAdapter = getuser?.let {
            UserRecyclerAdapter(it,this)
        }
        mBinding.reUser.adapter = mAdapter
        mBinding.reUser.layoutManager = LinearLayoutManager(this)
        mBinding.reUser.setHasFixedSize(true)
        mBinding.reUser.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
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
        useradapter?.notifyDataSetChanged()
    }
}