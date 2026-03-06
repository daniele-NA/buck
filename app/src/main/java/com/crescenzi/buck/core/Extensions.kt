package com.crescenzi.buck.core

import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityManager

// == CHECK IF A SPECIFIC ACCESSIBILITY SERVICE IS ENABLED == //
fun AccessibilityManager.isServiceEnabled(serviceClass: Class<*>): Boolean {
    return getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
        .any { it.resolveInfo.serviceInfo.name == serviceClass.name }
}
