package com.crescenzi.buck.presentation

import android.app.Application
import android.view.accessibility.AccessibilityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.crescenzi.buck.BuckService
import com.crescenzi.buck.core.isServiceEnabled
import com.crescenzi.buck.data.PreferencesRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val prefsRepo by lazy { PreferencesRepo(app) }
    private val am by lazy { app.getSystemService(AccessibilityManager::class.java) }

    val serviceEnabled = MutableStateFlow(false)

    val triggerKey: StateFlow<String?> = prefsRepo.triggerKeyFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val writingEnabled: StateFlow<Boolean> = prefsRepo.writingEnabledFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun refreshServiceStatus() {
        serviceEnabled.value = am.isServiceEnabled(BuckService::class.java)
    }

    fun toggleWriting() {
        viewModelScope.launch { prefsRepo.setWritingEnabled(!writingEnabled.value) }
    }

    fun saveTriggerKey(key: String) {
        viewModelScope.launch { prefsRepo.setTriggerKey(key) }
    }
}
