package com.example.testagainaccessibility

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.testagainaccessibility.model.DataModel
import com.example.testagainaccessibility.utils.RequestPermission

class MainActivity : AppCompatActivity() {
    private val button: Button by lazy { findViewById<Button>(R.id.button) }
    private val button2: Button by lazy { findViewById<Button>(R.id.button2) }
    private val button3: Button by lazy { findViewById<Button>(R.id.button3) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            RequestPermission.requestAccessibility(it.context)
        }
        button2.setOnClickListener {
            RequestPermission.requestUsageAccess(it.context)
        }
        button3.setOnClickListener {
            startActivity(Intent(this,ListActivity::class.java))
        }
    }
}