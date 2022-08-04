package com.example.testagainaccessibility.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.testagainaccessibility.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AccsService : AccessibilityService() {
    var info = AccessibilityServiceInfo()
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val nodeInfo = event?.source ?: return
        Log.e("TAG", "onAccessibilityEvent: ${Constant.isCleanRunning}", )
        if (Constant.isCleanRunning) {
            CoroutineScope(Dispatchers.IO).launch{
                var list = nodeInfo.findAccessibilityNodeInfosByText(Constant.CACHE)
                if (list.isEmpty() || list.size == 0) {
                    performGlobalAction(GLOBAL_ACTION_BACK)
                    Log.e("ok", "back 1")
                }
                else {
                    for (node in list) {

                        if (node.isEnabled) {
                            Log.e("Click 1", "${node}")
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                            Log.e("ok", "click_force_stop")
                            val clickOK = node.findAccessibilityNodeInfosByText("Clear cache")
                            if (clickOK.isEmpty() || clickOK.size == 0) {
                                performGlobalAction(GLOBAL_ACTION_BACK)
                                Log.e("ok", "back 2")
                            }else{
                                for (btnCacheAnd in clickOK){
                                    if (btnCacheAnd.isEnabled){
                                        Log.e("clickOK", "${btnCacheAnd}")
                                        btnCacheAnd.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                                        list = nodeInfo.findAccessibilityNodeInfosByText((Constant.OK))
                                        for (node in list) {
                                            delay(500)
                                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                                            delay(500)
                                            performGlobalAction(GLOBAL_ACTION_BACK)
                                            Log.e("ok", "OKKKKK")
                                        }
                                    }
                                }

                            }
                        }
//                        else {
//                            performGlobalAction(GLOBAL_ACTION_BACK)
//                        }
                    }
                }
//                list = nodeInfo.findAccessibilityNodeInfosByText((Constant.OK))
//                for (node in list) {
//                    delay(500)
//                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
//                    performGlobalAction(GLOBAL_ACTION_BACK)
//                    Log.e("ok", "OKKKKK")
//                }
            }
        }

    }
    override fun onInterrupt() {
        Log.e("Accessibility", "Interrupt : wrong")
    }
    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = AccessibilityServiceInfo()
        info.apply {
            eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED or AccessibilityEvent.TYPE_VIEW_FOCUSED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN
             flags = AccessibilityServiceInfo.DEFAULT;
            notificationTimeout = 100
        }
        this.serviceInfo = info
        Log.e("Accessibility", "connected")
    }
}