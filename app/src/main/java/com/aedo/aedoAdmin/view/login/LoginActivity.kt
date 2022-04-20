package com.aedo.aedoAdmin.view.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.Html
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.api.APIService
import com.aedo.aedoAdmin.api.ApiUtils
import com.aedo.aedoAdmin.databinding.ActivityLoginBinding
import com.aedo.aedoAdmin.model.intro.Policy
import com.aedo.aedoAdmin.model.login.LoginSend
import com.aedo.aedoAdmin.model.login.UserModel
import com.aedo.aedoAdmin.util.base.BaseActivity
import com.aedo.aedoAdmin.util.base.MyApplication.Companion.prefs
import com.aedo.aedoAdmin.util.encrypt.AESAdapter
import com.aedo.aedoAdmin.util.encrypt.Base64Util
import com.aedo.aedoAdmin.util.log.LLog
import com.aedo.aedoAdmin.util.log.LLog.TAG
import com.aedo.aedoAdmin.view.intro.permission.PermissionManager
import com.aedo.aedoAdmin.viewmodel.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var apiServices: APIService
    private var mViewModel: LoginViewModel? = null
    private val adminKey = "true"

    companion object {
        const val PROCESS_PHONENUM = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding.activity = this
        mBinding.viewModel = mViewModel
        apiServices = ApiUtils.apiService
        mBinding.tvTitle.text = Html.fromHtml(getString(R.string.login_desc1))
        mBinding.tvTitleSub.text = getText(R.string.login_subtitle1)
        inStatusBar()
        initView()
    }

    private fun initView() {
        startbtn()
        callresult()
        if (PermissionManager.getPermissionGranted(this, Manifest.permission.READ_PHONE_STATE)) {
            val manager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_SMS
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_NUMBERS
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            @SuppressLint("HardwareIds") var telNo = manager.line1Number
            if (telNo != null) {
                telNo = telNo.replace("+82", "0")
                mViewModel!!.setPhoneNum(telNo)
            }
        }

        if (comm!!.defaultPhoneNumber != null && comm!!.defaultPhoneNumber!!.isNotEmpty()) {
            val encResult: String =
                comm!!.defaultPhoneNumber!!.substring(comm!!.defaultPhoneNumber!!.length - 2)
            // 암호화
            val resultText: String? = when {
                encResult.contains("==") -> {
                    AESAdapter.decAES(prefs.getEncIv()!!, prefs.getEncKey()!!, comm!!.defaultPhoneNumber)
                } // Base64
                encResult.contains("=") -> {
                    Base64Util.decode(comm!!.defaultPhoneNumber)
                } // plane text
                else -> {
                    comm!!.defaultPhoneNumber
                }
            }
            mViewModel!!.setPhoneNum(resultText)
        }
    }

    private fun startbtn() {
        val phoneAgainSpan = SpannableString(mBinding.tvPhonenumInputAgain.text)
        var start = 0
        var end = mBinding.tvPhonenumInputAgain.length()

        val phoneClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
            override fun onClick(widget: View) {
                phoneFirst(true)
            }
        }
        phoneAgainSpan.setSpan(phoneClickableSpan, start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        phoneAgainSpan.setSpan(ForegroundColorSpan(getColor(R.color.gray2)), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        start = 0
        end = mBinding.tvAuthnumRequestAgain.length()
        mBinding.tvPhonenumInputAgain.text = phoneAgainSpan
        mBinding.tvPhonenumInputAgain.isClickable = true
        mBinding.tvPhonenumInputAgain.movementMethod = LinkMovementMethod.getInstance()
        mBinding.tvPhonenumInputAgain.isClickable = true
        mBinding.tvPhonenumInputAgain.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun callresult() {
        ResultView =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
            }
    }

    private fun phoneFirst(resetPhoneNum: Boolean) {
        mViewModel?.loginProcess = PROCESS_PHONENUM
        mViewModel!!.authNum = ""
        if (resetPhoneNum) {
            mViewModel!!.setPhoneNum("")
        }
        if (mBinding.tvTitleSub.visibility == View.GONE) {
            mBinding.tvTitleSub.visibility = View.VISIBLE
        }
        mBinding.tvTitle.text = getString(R.string.login_desc1)
        mBinding.tvTitleSub.text = getText(R.string.login_subtitle1)
        mBinding.btnOk.text = getString(R.string.login_btn_sms_send)
    }

    private fun phoneSecond() {
        if(mBinding.clAuthNumParent.visibility== View.GONE) {
            mBinding.clAuthNumParent.visibility= View.VISIBLE
            mBinding.tvPhonenumAuth.text = mBinding.etPhonenum.text.toString()
        }
        mBinding.tvTitle.text = getString(R.string.login_desc2)
        mBinding.tvTitleSub.text = getText(R.string.login_subtitle2)
        if (mBinding.tvTitleSub.visibility == View.GONE) {
            mBinding.tvTitleSub.visibility = View.VISIBLE
        }
        if(mBinding.btnOk2.visibility == View.GONE) {
            mBinding.btnOk2.visibility = View.GONE
        }
        mBinding.btnOk.text = getString(R.string.ok)
        mBinding.etAuthnum.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }


    private fun phoneThrid() {
        mBinding.tvTitle.text = Html.fromHtml(getString(R.string.login_desc3))
        mBinding.tvTitleSub.text = getText(R.string.login_subtitle3)
        if (mBinding.tvTitleSub.visibility == View.GONE) {
            mBinding.tvTitleSub.visibility = View.VISIBLE
        }
        if(mBinding.clCheck.visibility == View.GONE) {
            mBinding.clCheck.visibility = View.VISIBLE
        }
        if (mBinding.clOk2.visibility == View.GONE) {
            mBinding.clOk2.visibility = View.VISIBLE
        }
        if (mBinding.btnOk2.visibility == View.GONE) {
            mBinding.btnOk2.visibility = View.VISIBLE
        }

        if(mBinding.clAuthNumParent.visibility == View.VISIBLE) {
            mBinding.clAuthNumParent.visibility = View.GONE
            mBinding.tvPhonenumAuth.text = mBinding.etPhonenum.text.toString()
        }
        mBinding.btnOk.text = getString(R.string.ok)
        mBinding.clOk.visibility = View.GONE
        mBinding.clOk2.visibility = View.VISIBLE

    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val side = Rect()
                val btn = Rect()
                v.getGlobalVisibleRect(side)
                mBinding.btnOk.getGlobalVisibleRect(btn)
                if (!side.contains(ev.rawX.toInt(), ev.rawY.toInt()) && !btn.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun authrequest() {
        LLog.e("로그인_로그인 API")
        val phone = LoginSend(phone = mBinding.etPhonenum.text.toString())
        apiServices.getLogin(phone).enqueue(object : Callback<LoginSend> {
            override fun onResponse(call: Call<LoginSend>, response: Response<LoginSend>) {
                val result = response.body()
                if(response.isSuccessful&&result!=null) {
                    prefs.myaccesstoken = result.accesstoken.toString()
                    userAPI()
                }
                else {
                    if (response.code() == 404) {
                        phoneSecond()
                        prefs.myaccesstoken = result?.accesstoken.toString()
                    }
                }
            }
            override fun onFailure(call: Call<LoginSend>, t: Throwable) {
                Log.d(TAG,"authrequest ERROR -> $t")
            }
        })
    }

    private fun userAPI() {
        LLog.e("회원정보 API")
        apiServices.getUser(prefs.myaccesstoken).enqueue(object :
            Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                   if (result.admin == false) {
                       moveMain()
                   }
                    else {
                        adminLogin()
                   }
                }
                else {
                    Log.d(TAG,"GetUser API ERROR -> ${response.errorBody()}")
                    otherAPI()
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.d(TAG,"GetUser ERROR -> $t")

            }
        })
    }

    private fun otherAPI() {
        LLog.e("회원정보_두번째 API")
        apiServices.getUser(prefs.myaccesstoken).enqueue(object :
            Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    if (result.admin == false) {
                        moveMain()
                    }
                    else {
                        adminLogin()
                    }
                }
                else {
                    Log.d(TAG,"GetUser SECOND API ERROR -> ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.d(TAG,"GetUser SECOND ERROR -> $t")

            }
        })
    }

    fun onClearPhoneClick(v: View?) {
        mViewModel!!.setPhoneNum("")
    }

    fun onClearAuthClick(v: View?) {
        mViewModel!!.setAuthNum("")

    }

    fun onCheckButtonClick(v: View) {
        mBinding.btnAgree.isSelected = !mBinding.btnAgree.isSelected
    }

    fun onCheckClick(v: View) {
        val et_auth = mBinding.etAuthnum.text.toString()
        if (prefs.mysms.equals(et_auth)) {
            phoneThrid()
        }
        else {
            smscheck()
        }
    }

    fun onOkClick(v: View?) {

    }

    fun onSendClick(v: View?){
        val et_phone = mBinding.etPhonenum.text.toString()
        if (et_phone.length<10) {
            phonecheck()
        }
        else {
            authrequest()
        }
    }
}