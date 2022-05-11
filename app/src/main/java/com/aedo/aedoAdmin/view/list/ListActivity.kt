package com.aedo.aedoAdmin.view.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.api.APIService
import com.aedo.aedoAdmin.api.ApiUtils
import com.aedo.aedoAdmin.databinding.ActivityListBinding
import com.aedo.aedoAdmin.util.base.BaseActivity
import com.aedo.aedoAdmin.util.base.MyApplication.Companion.prefs
import com.aedo.aedoAdmin.util.log.LLog
import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aedo.aedoAdmin.adapter.ObituaryRecyclerAdapter
import com.aedo.aedoAdmin.model.list.Obituary
import com.aedo.aedoAdmin.model.list.RecyclerList
//import com.aedo.aedoAdmin.util.alert.LoadingDialog
//import com.aedo.aedoAdmin.util.base.BaseActivity
import com.aedo.aedoAdmin.util.log.LLog.TAG
//import com.aedo.aedoAdmin.view.main.MainActivity
//import com.kakao.sdk.common.KakaoSdk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListActivity : BaseActivity() {
    private lateinit var mBinding : ActivityListBinding
    private lateinit var apiServices: APIService  //클래스 APIService 객체

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_list) //view와 연결
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        inStatusBar()
        inRecycler()
    }
    //리사이클러뷰 데이터 받아오는 API
    private fun inRecycler() {
        LLog.e("부고조회_첫번째 API")
        val vercall: Call<RecyclerList> = apiServices.getObituary("",prefs.myaccesstoken)
        vercall.enqueue(object : Callback<RecyclerList> {
            override fun onResponse(call: Call<RecyclerList>, response: Response<RecyclerList>) {
                val result = response.body()
                Log.d(TAG,"List response -> $response")
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"List response SUCCESS -> $result")
                    setAdapter(result.result)
                }
                else {
                    Log.d(TAG,"List response ERROR -> $result")
                    otherAPI()
                }
            }
            override fun onFailure(call: Call<RecyclerList>, t: Throwable) {
                Log.d(TAG, "List error -> $t")
            }
        })
    }

    private fun otherAPI() {
        LLog.e("부고조회_두번째 API")
        val vercall: Call<RecyclerList> = apiServices.getObituary("",prefs.newaccesstoken)
        vercall.enqueue(object : Callback<RecyclerList> {
            override fun onResponse(call: Call<RecyclerList>, response: Response<RecyclerList>) {
                val result = response.body()
                Log.d(TAG,"List Second response -> $response")
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"List Second response SUCCESS -> $result")
                    setAdapter(result.result)
                }
                else {
                    Log.d(TAG,"List Second esponse ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<RecyclerList>, t: Throwable) {
                Log.d(TAG, "List Second Fail -> $t")
            }
        })
    }

    private fun setAdapter(obituary: List<Obituary>?){
        val mAdapter = obituary?.let {
            ObituaryRecyclerAdapter(it,this)
        }
        mBinding.recyclerView.adapter = mAdapter
        mBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mBinding.recyclerView.setHasFixedSize(true)
        mBinding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        return
    }

    fun onMainClick(v: View) {
        moveMain()
    }

    fun onBackClick(v: View) {
        moveMain()
    }
}