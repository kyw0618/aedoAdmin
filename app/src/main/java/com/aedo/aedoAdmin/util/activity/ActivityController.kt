package com.aedo.aedoAdmin.util.activity

import android.content.Intent
import android.os.Bundle
import com.aedo.aedoAdmin.util.`object`.Constant
import com.aedo.aedoAdmin.util.base.BaseActivity
import com.aedo.aedoAdmin.util.base.MyApplication.Companion.prefs
import com.aedo.aedoAdmin.view.intro.SplashActivity
import com.aedo.aedoAdmin.view.intro.permission.PermissionActivity

class ActivityController : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        if (intent != null) {
            val deeplinkUri = intent.data
            if (deeplinkUri != null && deeplinkUri.isHierarchical) {
                val deeplinkString = intent.dataString
                if (deeplinkString!!.contains("phone=")) {
                    val splitResult = deeplinkString.split("phone=").toTypedArray()
                    if (splitResult.size > 1) {
                        comm?.defaultPhoneNumber = splitResult[1]
                    }
                }
            }
        }
        var next: Class<*> = SplashActivity::class.java
        if (!prefs.getBool(Constant.PREF_PERMISSION_GRANTED)) {
            next = PermissionActivity::class.java
        }
        startActivity(Intent(this, next))
        finish()
    }
}