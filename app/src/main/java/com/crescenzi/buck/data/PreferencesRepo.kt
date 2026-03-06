package com.crescenzi.buck.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore(name = "buck_prefs")

class PreferencesRepo(private val context: Context) {

    companion object {
        private val TRIGGER_KEY = stringPreferencesKey("trigger_key")
        private val WRITING_ENABLED = booleanPreferencesKey("writing_enabled")
    }

    val triggerKeyFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[TRIGGER_KEY]
    }

    val writingEnabledFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[WRITING_ENABLED] ?: false
    }

    // == BLOCKING READS FOR USE IN ACCESSIBILITY SERVICE == //
    fun getTriggerKeySync(): String? = runBlocking {
        triggerKeyFlow.first()
    }

    fun isWritingEnabledSync(): Boolean = runBlocking {
        writingEnabledFlow.first()
    }

    suspend fun setTriggerKey(key: String) {
        context.dataStore.edit { prefs ->
            prefs[TRIGGER_KEY] = key
        }
    }

    suspend fun setWritingEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[WRITING_ENABLED] = enabled
        }
    }
}
