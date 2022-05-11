package com.aedo.aedoAdmin.util.base

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.*
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import com.aedo.aedoAdmin.R
import com.aedo.aedoAdmin.util.`object`.ActivityControlManager
import com.aedo.aedoAdmin.util.common.CommonData
import com.aedo.aedoAdmin.util.log.LLog.e
import com.aedo.aedoAdmin.view.list.ListActivity
import com.aedo.aedoAdmin.view.list.ListDetailActivity
import com.aedo.aedoAdmin.view.main.MainActivity
import com.aedo.aedoAdmin.view.notice.NoticeActivity
import com.aedo.aedoAdmin.view.order.OrderActivity
import com.aedo.aedoAdmin.view.user.UserActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.one_button_dialog.view.*
import kotlinx.android.synthetic.main.two_button_dialog.view.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

open class BaseActivity : AppCompatActivity() {
    internal open var instance: BaseActivity? = null
    var ResultView: ActivityResultLauncher<Intent>? = null
    var comm: CommonData? = CommonData().getInstance()

    internal var dialog: Dialog? = null

    internal val realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        instance = this
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    internal fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.progress)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onResume() {
        super.onResume()
        if (isInternetAvailable(this)) {
            Log.d(TAG, "네트워크 연결중")
        } else {
            networkDialog()
            return
        }
    }

    open fun onTitleLeftClick(v: View?) {}

    open fun setWindowFlag(activity: AppCompatActivity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    open fun moveAndFinishActivity(activity: Class<*>?) {
        ActivityControlManager.moveAndFinishActivity(this, activity)
    }

    open fun hideSoftKeyboard(v: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    open fun hideSoftKeyboard() {
        val v = currentFocus
        if (v != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }


    @SuppressLint("MissingPermission")
    fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }

    val hash: String?
        get() {
            val context = applicationContext
            val pm = context.packageManager
            val packageName = context.packageName
            var cert: String? = null
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    @SuppressLint("WrongConstant") val packageInfo = packageManager.getPackageInfo(
                        getPackageName(),
                        PackageManager.GET_SIGNING_CERTIFICATES
                    )
                    val signatures = packageInfo.signingInfo.apkContentsSigners
                    val md = MessageDigest.getInstance("SHA1")
                    for (signature in signatures) {
                        md.update(signature.toByteArray())
                        cert = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                        cert = cert.replace(
                            Objects.requireNonNull(System.getProperty("line.separator")).toRegex(),
                            ""
                        )
                    }
                } else {
                    @SuppressLint("PackageManagerGetSignatures") val packageInfo =
                        pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
                    val certSignature = packageInfo.signatures[0]
                    val msgDigest = MessageDigest.getInstance("SHA1")
                    msgDigest.update(certSignature.toByteArray())
                    cert = Base64.encodeToString(msgDigest.digest(), Base64.DEFAULT)
                    cert = cert.replace(
                        Objects.requireNonNull(System.getProperty("line.separator")).toRegex(), ""
                    )
                    Log.i("test", "Cert=$cert")
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e("예외발생")
            } catch (e: NoSuchAlgorithmException) {
                e("예외발생")
            }
            return cert
        }

    companion object {
        fun setWindowFlag(activity: AppCompatActivity, bits: Int, on: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }

    @SuppressLint("ResourceType")
    internal fun serverDialog() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView: TextView = myLayout.findViewById(R.id.popTv_one)
        textView.text = getString(R.string.server_check)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            finish()
            dialog.dismiss()
        }

    }

    internal fun networkDialog() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView: TextView = myLayout.findViewById(R.id.popTv_one)
        textView.text = getString(R.string.network_check)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            finish()
            dialog.dismiss()
        }
    }

    internal fun rootingDialog() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView: TextView = myLayout.findViewById(R.id.popTv_one)
        textView.text = getString(R.string.warning_rooting)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            finish()
            dialog.dismiss()
        }
    }

    internal fun smscheck() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView: TextView = myLayout.findViewById(R.id.popTv_one)
        textView.text = getString(R.string.auth_num_wrong_text)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    internal fun adminLogin() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView: TextView = myLayout.findViewById(R.id.popTv_one)
        textView.text = getString(R.string.admin_check)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    internal fun termcheck() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView: TextView = myLayout.findViewById(R.id.popTv_one)
        textView.text = getString(R.string.term_check)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    internal fun phonecheck() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView: TextView = myLayout.findViewById(R.id.popTv_one)
        textView.text = getString(R.string.phone_check)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    internal fun UpdateDialog() {
        val myLayout = layoutInflater.inflate(R.layout.two_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView: TextView = myLayout.findViewById(R.id.popTv_second)
        textView.text = getString(R.string.update_check)
        val dialog = build.create()
        dialog.show()

        myLayout.finish_btn.setOnClickListener {
            finish()
            dialog.dismiss()
        }
        myLayout.update_btn.setOnClickListener {
            finish()
            dialog.dismiss()
        }
    }

    internal fun listdelete() {
        val myLayout = layoutInflater.inflate(R.layout.two_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView: TextView = myLayout.findViewById(R.id.popTv_second)
        textView.text = getString(R.string.list_delete)
        val dialog = build.create()
        dialog.show()

        myLayout.finish_btn.text = getString(R.string.btn_delete)
        myLayout.update_btn.text = getString(R.string.btn_modify)

        myLayout.finish_btn.setOnClickListener {
            finish()
            dialog.dismiss()
        }
        myLayout.update_btn.setOnClickListener {
            finish()
            dialog.dismiss()
        }
    }

    internal fun moveMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveUser() {
        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveOrder() {
        val intent = Intent(this, OrderActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveList() {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveNotice() {
        val intent = Intent(this, NoticeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }
}