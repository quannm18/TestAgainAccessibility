package com.example.testagainaccessibility.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity

class RequestPermission {
    companion object{
        const val READ_WRITE_CODE = 2296
        @JvmStatic
        fun requestReadAndWriteStorage(activity: Activity){
            ActivityCompat.requestPermissions(
                activity, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), READ_WRITE_CODE
            )
        }

        @JvmStatic
        fun requestUsageAccess(mContext: Context){
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            mContext.startActivity(intent)
        }

        @JvmStatic
        fun requestAccessibility(mContext: Context){
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            mContext.startActivity(intent)
        }

    }
}