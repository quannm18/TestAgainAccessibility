package com.example.testagainaccessibility

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testagainaccessibility.adapter.adapter.MyAdapter
import com.example.testagainaccessibility.listeners.ICheckedListener
import com.example.testagainaccessibility.model.DataModel
import com.example.testagainaccessibility.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListActivity : AppCompatActivity() , ICheckedListener{
    private val rcvMain: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rcvMain) }
    private val btnAllChecked: Button by lazy { findViewById<Button>(R.id.btnAllChecked) }
    private var mListLiveData: MutableLiveData<MutableList<DataModel>> = MutableLiveData()
    private var mTemptList: MutableList<DataModel> = mutableListOf()
    private var mAdapter: MyAdapter = MyAdapter()

    private val btnStart: Button by lazy { findViewById<Button>(R.id.btnStart) }
    private var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        mTemptList = convertListInstallToDataList(getListInstalledApps(false,true))
        Constant.isCheckedAll.set(false)
        rcvMain.apply {
            layoutManager = LinearLayoutManager(this@ListActivity)
            adapter = mAdapter
            addItemDecoration(DividerItemDecoration(this@ListActivity, DividerItemDecoration.VERTICAL))
        }
        mListLiveData = liveDataInit(mTemptList)
        mListLiveData.observe(this) {
            mAdapter.initData(it,this)
            mAdapter.notifyDataSetChanged()
        }
        btnAllChecked.setOnClickListener {
            Constant.isCheckedAll.set(!Constant.isCheckedAll.get())
            mTemptList.forEach {
                it.status = Constant.isCheckedAll.get()
            }
            mListLiveData.postValue(mTemptList)
        }
        btnStart.setOnClickListener {
            i = 0
            Constant.isCleanRunning = true
            CoroutineScope(Dispatchers.IO).launch {
                cleanApp()
            }
        }
    }

    fun convertListInstallToDataList(installList: ArrayList<PackageInfo>): MutableList<DataModel> {
        val mTempList: MutableList<DataModel> = mutableListOf()
        installList.forEach {
            val res = packageManager.getResourcesForApplication(it.applicationInfo)
            val resID = it.applicationInfo.labelRes
            val name = res.getString(resID)
            mTempList.add(
                DataModel(
                    name = name,
                    packageName = it.packageName,
                    status = false
                )
            )
        }

        return mTempList
    }

    fun liveDataInit(mList: MutableList<DataModel>): MutableLiveData<MutableList<DataModel>> {
        val liveList: MutableLiveData<MutableList<DataModel>> = MutableLiveData()
        liveList.postValue(mList)
        return liveList
    }

    private fun getListInstalledApps(
        systemOnly: Boolean,
        userOnly: Boolean
    ): ArrayList<PackageInfo> {
        val list = packageManager.getInstalledPackages(0)
        val pkgInfoList = ArrayList<PackageInfo>()
        for (i in list.indices) {
            val packageInfo = list[i]
            val flags = packageInfo!!.applicationInfo.flags
            val isSystemApp = (flags and ApplicationInfo.FLAG_SYSTEM) != 0
            val isUpdatedSystemApp = (flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
            val addPkg = (systemOnly && (isSystemApp and !isUpdatedSystemApp)) or
                    (userOnly && (!isSystemApp or isUpdatedSystemApp)) and (packageInfo.packageName != "com.example.antivirus_pro") and
                    (packageInfo.packageName != "com.google.android.gms") and (packageInfo.packageName != "com.android.vending") and
                    (packageInfo.packageName != "com.google.android.") and (packageInfo.packageName != packageName)
            if (addPkg)
                pkgInfoList.add(packageInfo)
        }
        return pkgInfoList
    }

    override fun onQualityChange(mList: HashSet<String>) {
        Toast.makeText(this,"${mList.toString()}",Toast.LENGTH_SHORT).show()

    }

    private fun startApplicationDetailsActivity(packageName: String) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
    private fun cleanApp() {
        val mList = mAdapter.checkList
        try {
            if (i<mList.size){
                startApplicationDetailsActivity(mList.elementAt(i))
                i++
            }else{
                Constant.isCleanRunning = false
            }
        } catch (e: Exception) {
            Constant.isCleanRunning = false
            startActivity(Intent(this, MainActivity::class.java))
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        if (i>0){
            CoroutineScope(Dispatchers.IO).launch {
                delay(500)
                cleanApp()
                delay(500)
            }
        }else{
            if (i==mTemptList.size){

            }
        }
    }

}