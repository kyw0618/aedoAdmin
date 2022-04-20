package com.aedo.aedoAdmin.view.intro.permission

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.content.Context
import com.aedo.aedoAdmin.util.`object`.Constant

object PermissionManager {

    fun getPermissionGranted(context: Context?, permissionCode: String?): Boolean {
        return ContextCompat.checkSelfPermission(
            context!!,
            permissionCode!!
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getAllPermissionGranted(context: Context?): Boolean {
        var allGranted = true
        for (permissionCode in Constant.MUTILE_PERMISSION) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    permissionCode!!
                ) == PackageManager.PERMISSION_DENIED
            ) {
                allGranted = false
            }
        }
        return allGranted
    }

    fun requestAllPermissions(context: Context?) {
        val arraySize: Int = Constant.MUTILE_PERMISSION.size
        ActivityCompat.requestPermissions(
            (context as AppCompatActivity?)!!,
            Constant.MUTILE_PERMISSION.toArray(arrayOfNulls<String>(arraySize)),
            Constant.ALL_PERMISSION_REQUEST_CODE
        )
    }
}