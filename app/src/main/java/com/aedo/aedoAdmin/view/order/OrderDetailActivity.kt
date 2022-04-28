package com.aedo.aedoAdmin.view.order

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.api.APIService
import com.aedo.aedoAdmin.api.ApiUtils
import com.aedo.aedoAdmin.databinding.ActivityOrderDetailBinding
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_CREATED
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_ITEM
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_PLACE_NAME
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_PLACE_NUMBER
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_PRICE
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_RECEIVER_NAME
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_RECEIVER_PHONE
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_SENDER_NAME
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_SENDER_PHONE
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_WORD_COMPANY
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_WORD_WORD
import com.aedo.aedoAdmin.util.`object`.Constant.MY_ORDER_WORD_WORDSECOND
import com.aedo.aedoAdmin.util.base.BaseActivity

class OrderDetailActivity : BaseActivity() {
    private lateinit var mBinding : ActivityOrderDetailBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val place_name = intent.getStringExtra(MY_ORDER_PLACE_NAME)
        val place_number = intent.getStringExtra(MY_ORDER_PLACE_NUMBER)
        val item = intent.getStringExtra(MY_ORDER_ITEM)
        val price = intent.getStringExtra(MY_ORDER_PRICE)
        val receiver_name = intent.getStringExtra(MY_ORDER_RECEIVER_NAME)
        val receiver_phone = intent.getStringExtra(MY_ORDER_RECEIVER_PHONE)
        val sender_name = intent.getStringExtra(MY_ORDER_SENDER_NAME)
        val sender_phone = intent.getStringExtra(MY_ORDER_SENDER_PHONE)
        val company = intent.getStringExtra(MY_ORDER_WORD_COMPANY)
        val word = intent.getStringExtra(MY_ORDER_WORD_WORD)
        val word_second = intent.getStringExtra(MY_ORDER_WORD_WORDSECOND)
        val created = intent.getStringExtra(MY_ORDER_CREATED)

        mBinding.myorderFlower.text = item.toString()
        mBinding.tvMyorderCreate.text = created.toString()
        mBinding.tvMyorderPrice.text = price.toString()
        mBinding.tvMyorderReceiverName.text = receiver_name.toString()
        mBinding.tvMyorderReceiverPhone.text = receiver_phone.toString()
        mBinding.tvMyorderSendName.text = sender_name.toString()
        mBinding.tvMyorderSendPhone.text = sender_phone.toString()
        mBinding.tvMyorderPlace.text = place_name.toString()
        mBinding.tvMyorderPlaceNumber.text = place_number.toString()
        mBinding.tvMyorderCompany.text = company.toString()
        mBinding.tvMyorderWord.text = word.toString()
        mBinding.tvMyorderWordStright.text = word_second.toString()
    }

    fun onBackClick(v: View) {
        moveOrder()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, OrderActivity::class.java))
        finish()
    }
}