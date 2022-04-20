package com.aedo.aedoAdmin.view.intro.permission

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.databinding.DataBindingUtil
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.databinding.ActivityMainBinding
import com.aedo.aedoAdmin.databinding.ActivityPermissionBinding
import com.aedo.aedoAdmin.util.`object`.Constant
import com.aedo.aedoAdmin.util.base.BaseActivity
import com.aedo.aedoAdmin.util.base.MyApplication.Companion.prefs
import com.aedo.aedoAdmin.view.intro.SplashActivity

class PermissionActivity : BaseActivity() {
    private lateinit var mBinding : ActivityPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_permission)
        mBinding.activity = this
        inStatusBar()
    }

    fun onConfirmClick(v: View?) {
        if (!PermissionManager.getAllPermissionGranted(this)) {
            PermissionManager.requestAllPermissions(this)
        } else {
            prefs.setBool(Constant.PREF_PERMISSION_GRANTED, true)
            moveActivity(b = true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.ALL_PERMISSION_REQUEST_CODE) {
            if (PermissionManager.getAllPermissionGranted(this)) {
                prefs.setBool(Constant.PREF_PERMISSION_GRANTED, true)
                moveActivity(b = true)
            }
            else {
                if (getPermissionAllGranted()) {
                }
                else {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            }
        }
    }

    private fun getPermissionAllGranted(): Boolean {
        var permissonNotShowDenied = true
        for (permission in Constant.MUTILE_PERMISSION) {
            if (!permission?.let {
                    shouldShowRequestPermissionRationale(it)
                }!!) {
                permissonNotShowDenied = false
            }
        }
        return permissonNotShowDenied
    }

    private fun moveActivity(b: Boolean) {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }
}