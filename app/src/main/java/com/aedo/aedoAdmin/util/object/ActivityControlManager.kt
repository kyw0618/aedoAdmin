package com.aedo.aedoAdmin.util.`object`

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Handler
import android.os.Looper

object ActivityControlManager {

    fun moveAndFinishActivity(c: AppCompatActivity, activity: Class<*>?) {
        val intent = Intent(c, activity)
        c.startActivity(intent)
        c.overridePendingTransition(0, 0)
        c.finish()
    }

    fun moveActivity(c: AppCompatActivity, activity: Class<*>?) {
        val intent = Intent(c, activity)
        c.startActivity(intent)
        c.overridePendingTransition(0, 0)
    }


    fun delayRun(r: Runnable?, delay: Int) {
        val loop = Looper.myLooper()
        if (loop != null) {
            val handler = Handler(loop)
            handler.postDelayed(r!!, delay.toLong())
        }
    }
}