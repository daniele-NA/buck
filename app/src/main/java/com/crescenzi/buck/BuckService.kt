package com.crescenzi.buck

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.crescenzi.buck.core.LOG
import com.crescenzi.buck.data.ApiRepo
import com.crescenzi.buck.data.PreferencesRepo
import kotlin.concurrent.thread

@SuppressLint("AccessibilityPolicy")
class BuckService : AccessibilityService() {

    private val apiRepo = ApiRepo(apiKey = BuildConfig.COHERE_API_KEY)
    private lateinit var prefsRepo: PreferencesRepo

    override fun onCreate() {
        super.onCreate()
        prefsRepo = PreferencesRepo(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType != AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) return
        val node = event.source ?: return
        val text = event.text.joinToString(" ")

        // == CHECK IF WRITING IS ENABLED == //
        if (!prefsRepo.isWritingEnabledSync()) return

        // == READ TRIGGER KEY FROM PREFERENCES == //
        val key = prefsRepo.getTriggerKeySync()
        if (key.isNullOrEmpty() || !text.contains(key, ignoreCase = true)) return
        LOG("START FLOW")

        // == RUN ON A BACKGROUND THREAD TO KEEP THE NODE ALIVE DURING STREAMING == //
        thread {
            var currentText = ""
            apiRepo.call(text.replace(key, "")) { token ->
                currentText += token
                val args = Bundle().apply {
                    putCharSequence(
                        AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                        currentText
                    )
                }
                node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)
                Thread.sleep(45)
            }
        }
    }

    override fun onInterrupt() = Unit
}
