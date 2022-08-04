package com.example.testagainaccessibility.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.testagainaccessibility.utils.Constant

class AccsService : AccessibilityService() {
    var info = AccessibilityServiceInfo()
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val nodeInfo = event?.source ?: return
        Log.e("TAG", "onAccessibilityEvent: ${Constant.isCleanRunning}", )
        if (Constant.isCleanRunning) {
//            if (Constant.isCleanRunning == true) {
//
            var list = nodeInfo.findAccessibilityNodeInfosByText("Storage & cache")

            Thread.sleep(500)
            if (list.isEmpty() || list.size == 0) {
                performGlobalAction(GLOBAL_ACTION_BACK)
                Log.e("ok", "azzzzzc")
            } else {
                for (node in list) {
                    if (node.isEnabled) {
                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        Log.e("ok", "click_force_stop")
                    } else {
                        performGlobalAction(GLOBAL_ACTION_BACK)
                    }
                }
            }
            Thread.sleep(1000)
            val list1 = nodeInfo.findAccessibilityNodeInfosByText("Clear cache")

            Thread.sleep(500)
            if (list1.isEmpty() || list1.size == 0) {
                performGlobalAction(GLOBAL_ACTION_BACK)
                Log.e("ok", "azzzzzc")
            } else {
                for (node in list1) {
                    if (node.isEnabled) {
                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        Log.e("ok", "click_force_stop")
                    } else {
                        performGlobalAction(GLOBAL_ACTION_BACK)
                    }
                }
            }
            val listOK = nodeInfo.findAccessibilityNodeInfosByText("OK")
            Thread.sleep(300)
            for (node in listOK) {
                for (node in list1) {
                    if (node.isEnabled) {
                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        Log.e("ok", "click_force_stop")
                    } else {
                        performGlobalAction(GLOBAL_ACTION_BACK)
                    }
                }
            }
//            }

            for (i in 0 until nodeInfo.childCount) {
                Log.e("nodeInfo", "${nodeInfo.getChild(i)}")
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
            // flags = AccessibilityServiceInfo.DEFAULT;
            notificationTimeout = 100
        }
        this.serviceInfo = info
        Log.e("Access", "onServiceConnected")
        super.onServiceConnected()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            Log.e("Accessibility", "connected")
    }
}