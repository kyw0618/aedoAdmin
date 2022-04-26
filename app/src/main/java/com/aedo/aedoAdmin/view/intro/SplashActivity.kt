package com.aedo.aedoAdmin.view.intro

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.api.APIService
import com.aedo.aedoAdmin.api.ApiUtils
import com.aedo.aedoAdmin.databinding.ActivityMainBinding
import com.aedo.aedoAdmin.databinding.ActivitySplashBinding
import com.aedo.aedoAdmin.model.intro.*
import com.aedo.aedoAdmin.model.login.AutoLogin
import com.aedo.aedoAdmin.model.login.UserModel
import com.aedo.aedoAdmin.util.`object`.ActivityControlManager
import com.aedo.aedoAdmin.util.`object`.Constant
import com.aedo.aedoAdmin.util.`object`.Constant.RESULT_TRUE
import com.aedo.aedoAdmin.util.base.BaseActivity
import com.aedo.aedoAdmin.util.base.MyApplication
import com.aedo.aedoAdmin.util.log.LLog
import com.aedo.aedoAdmin.util.log.LLog.TAG
import com.aedo.aedoAdmin.util.network.ResultListener
import com.aedo.aedoAdmin.util.root.RootUtil
import com.aedo.aedoAdmin.view.login.LoginActivity
import com.getkeepsafe.relinker.BuildConfig
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.TextStyle

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private lateinit var mBinding : ActivitySplashBinding
    private lateinit var apiServices: APIService
    private var devpolicyversion: String? = null
    private var prefs = MyApplication.prefs
    private var appUpdate : AppUpdateManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
        checkNetwork()
//        moveActivity()
    }

    // 네트워크 체크
    private fun checkNetwork() {
        LLog.e("1. 네트워크 확인")
        if(isInternetAvailable(this)) {
            checkVerification()
        } else {
            networkDialog()
            return
        }
    }

    // 3. 검증 API 호출키 및 Hash키 검증
    private fun checkVerification() {
        LLog.e("3. 검증 API 호출키 및 Hash키 검증")
        requestVerificationJson(object : ResultListener {
            override fun onSuccess() {
                checkPolicyVersion()
            }
        })
    }

    // 4. 정책 버전 비교 및 저장
    private fun checkPolicyVersion() {
        LLog.e("4. 정책 버전 비교 및 저장")
        requestPolicy(object : ResultListener {
            override fun onSuccess() {
                enablecheck()
            }
        })
    }

    private fun requestVerificationJson(listener: ResultListener) {
        LLog.e("검증 API")
        val vercall: Call<Verification> = apiServices.getVerification("qwer")
        vercall.enqueue(object : Callback<Verification> {
            override fun onResponse(call: Call<Verification>, response: Response<Verification>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    val encrypt = Encrypt()
                    encrypt.key.toString()
                    encrypt.iv.toString()
                    result.result
                    result.encrypt
                    result.policy_ver
                    Log.d(TAG,"Vertification result SUCESS -> $result")
                    if (result.result == RESULT_TRUE) {
                        devpolicyversion = result.policy_ver.toString()
                        getinformation(result, listener)
                    }
                    else {
                        serverDialog()
                    }
                }
                else {
                    serverDialog()
                }
            }
            override fun onFailure(call: Call<Verification>, t: Throwable) {
                Log.d(TAG, "loadVerAPI error -> $t")
                serverDialog()
            }
        })
    }

    private fun getinformation(result: Verification?, networkListener: ResultListener) {
        LLog.e("프리프런스")
        val aes_iv: String? = Encrypt().iv
        val aes_key: String? = Encrypt().key
        prefs.myeniv = aes_iv
        prefs.myenkey = aes_key
        prefs.mylangcode = "LANG_0001"
        prefs.myhashKey = hash.toString()
        networkListener.onSuccess()
    }

    private fun requestPolicy(listener: ResultListener) {
        LLog.e("정책 API")
        val vercall: Call<AppPolicy> = apiServices.getPolicy()
        vercall.enqueue(object : Callback<AppPolicy> {
            override fun onResponse(call: Call<AppPolicy>, response: Response<AppPolicy>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"Policy response SUCCESS -> $result")
                    realmPolicy(result,listener)
                    requestLogin()
                }
                else {
                    Log.d(TAG,"Policy response ERROR -> $result")
                    serverDialog()
                }
            }
            override fun onFailure(call: Call<AppPolicy>, t: Throwable) {
                Log.d(TAG, "Policy error -> $t")
                serverDialog()
            }
        })
    }

    private fun realmPolicy(result: AppPolicy, listener: ResultListener) {
        LLog.e("렐름")
        realm.executeTransaction {
            realm.where(Policy::class.java).findAll().deleteAllFromRealm()
            realm.where(Code::class.java).findAll().deleteAllFromRealm()
            realm.where(AppMenu::class.java).findAll().deleteAllFromRealm()
            realm.where(Coordinates::class.java).findAll().deleteAllFromRealm()
            realm.copyToRealm(result.policy!!)
            realm.copyToRealm(result.code!!)
            realm.copyToRealm(result.app_menu!!)
            realm.copyToRealm(result.coordinates!!)

        }
    }

    private fun requestLogin() {
        LLog.e("자동 로그인 API")
        val vercall: Call<AutoLogin> = apiServices.getautoLogin(prefs.myaccesstoken)
        vercall.enqueue(object : Callback<AutoLogin> {
            override fun onResponse(call: Call<AutoLogin>, response: Response<AutoLogin>) {
                val result = response.body()
                if (response.code() == 404 || response.code() == 401) {
                    prefs.newaccesstoken=result?.accesstoken
                    moveLogin()
                }
                else if(response.code() == 200){
                    getPreferences(0).edit().remove("PREF_ACCESS_TOKEN").apply()
                    prefs.newaccesstoken=result?.accesstoken
                    moveMain()
                    Toast.makeText(this@SplashActivity,"자동로그인이 되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<AutoLogin>, t: Throwable) {
                Log.d(TAG, "requestLogin error -> $t")
                serverDialog()
            }
        })
    }

    private fun enablecheck() {
        val useYn = realm.where(Policy::class.java).equalTo("id","APP_ENABLE_YN").findFirst()
        val popupText = realm.where(Policy::class.java).equalTo("id","APP_ENABLE_CONTENT").findFirst()
        if (useYn != null) {
            if (useYn.value.equals("Y")) {
                serverDialog()
                return
            }
        } else {
            serverDialog()
            return
        }
    }

    private fun moveLogin() {
        ActivityControlManager.delayRun({
            moveAndFinishActivity(LoginActivity::class.java) },
            Constant.SPLASH_WAIT
        )
    }

    override fun onResume() {
        super.onResume()
        appUpdate?.appUpdateInfo?.addOnSuccessListener {
                appUpdateInfo ->
            if(appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdate?.startUpdateFlowForResult(
                    appUpdateInfo, AppUpdateType.IMMEDIATE,this,Constant.APP_UPDATE
                )
            }
        }
    }
}