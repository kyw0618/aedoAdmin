package com.aedo.aedoAdmin.view.order

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
import com.aedo.aedoAdmin.adapter.OrderRecyclerAdapter
import com.aedo.aedoAdmin.adapter.UserRecyclerAdapter
import com.aedo.aedoAdmin.api.APIService
import com.aedo.aedoAdmin.api.ApiUtils
import com.aedo.aedoAdmin.databinding.ActivityMainBinding
import com.aedo.aedoAdmin.databinding.ActivityOrderBinding
import com.aedo.aedoAdmin.model.login.UserModel
import com.aedo.aedoAdmin.model.order.GetOrder
import com.aedo.aedoAdmin.model.order.OrderModel
import com.aedo.aedoAdmin.util.base.BaseActivity
import com.aedo.aedoAdmin.util.base.MyApplication
import com.aedo.aedoAdmin.util.log.LLog
import com.aedo.aedoAdmin.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderActivity : BaseActivity() {
    private lateinit var mBinding : ActivityOrderBinding
    private lateinit var apiServices: APIService
    private var orderAdapter: OrderRecyclerAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
        inRecycler()
    }

    private fun inRecycler() {
        LLog.e("주문조회_첫번째 API")
        val vercall: Call<OrderModel> = apiServices.getOrder(MyApplication.prefs.myaccesstoken)
        vercall.enqueue(object : Callback<OrderModel> {
            override fun onResponse(call: Call<OrderModel>, response: Response<OrderModel>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"getUser response SUCCESS -> $result")
                    setAdapter(result.result)
                }
                else {
                    Log.d(LLog.TAG,"getUser response ERROR -> $result")
                    otherAPI()
                }
            }
            override fun onFailure(call: Call<OrderModel>, t: Throwable) {
                Log.d(LLog.TAG, "getUser Fail -> $t")
            }
        })
    }

    private fun otherAPI() {
        LLog.e("주문조회_두번째 API")
        val vercall: Call<OrderModel> = apiServices.getOrder(MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<OrderModel> {
            override fun onResponse(call: Call<OrderModel>, response: Response<OrderModel>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"getUser Second response SUCCESS -> $result")
                    setAdapter(result.result)
                }
                else {
                    Log.d(LLog.TAG,"getUser Second response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<OrderModel>, t: Throwable) {
                Log.d(LLog.TAG, "getUser Second Fail -> $t")
            }
        })
    }

    private fun setAdapter(result: List<GetOrder>?) {
        val mAdapter = result?.let {
            OrderRecyclerAdapter(it,this)
        }
        mBinding.reOrder.adapter = mAdapter
        mBinding.reOrder.layoutManager = LinearLayoutManager(this)
        mBinding.reOrder.setHasFixedSize(true)
        mBinding.reOrder.addItemDecoration(
            DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)
        )
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
        orderAdapter?.notifyDataSetChanged()
    }
}