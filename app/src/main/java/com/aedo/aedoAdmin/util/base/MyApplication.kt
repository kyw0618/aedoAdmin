package com.aedo.aedoAdmin.util.base

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.app.Application
import android.os.Bundle
import com.aedo.aedoAdmin.util.common.CommonData
import com.aedo.aedoAdmin.util.preference.PreferenceManager
import com.iamport.sdk.domain.core.Iamport
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceManager
        private var isMainNoticeViewed = false

        @Synchronized
        fun setIsMainNoticeViewed(viewed: Boolean) {
            isMainNoticeViewed = viewed
        }

        @Synchronized
        fun isIsMainNoticeViewed(): Boolean {
            return isMainNoticeViewed
        }
    }

    override fun onCreate() {
        super.onCreate()
        Iamport.create(this)
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("test-data")
            .allowWritesOnUiThread(true)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
        prefs = PreferenceManager(applicationContext)
        val commonData: CommonData = CommonData().getInstance()
        commonData.numStarted = 0

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
            }

            override fun onActivityStarted(p0: Activity) {
                if (commonData.numStarted == 0) {
                    commonData.isMainRefresh = true
                    commonData.isForeground = true
                }
                commonData.numStarted++            }

            override fun onActivityResumed(p0: Activity) {
            }

            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStopped(p0: Activity) {
                commonData.numStarted--
                if (commonData.numStarted == 0) {
                    commonData.isForeground = false
                }            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityDestroyed(p0: Activity) {
            }
        })
    }
}