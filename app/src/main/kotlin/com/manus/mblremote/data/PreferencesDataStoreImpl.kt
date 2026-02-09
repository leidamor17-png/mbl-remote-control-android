package com.manus.mblremote.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manus.mblremote.model.BillingStatus
import com.manus.mblremote.model.RemoteDevice
import com.manus.mblremote.model.UserPreferences
import com.manus.mblremote.repository.PreferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "mbl_remote_preferences")

/**
 * Implementação de DataStore para armazenar preferências do usuário
 */
class PreferencesDataStoreImpl(private val context: Context) : PreferencesDataStore {

    private val gson = Gson()
    private val RECENT_DEVICES_KEY = stringPreferencesKey("recent_devices")
    private val USER_PREFERENCES_KEY = stringPreferencesKey("user_preferences")
    private val BILLING_STATUS_KEY = stringPreferencesKey("billing_status")

    override suspend fun getRecentDevices(): List<RemoteDevice> {
        return try {
            val preferences = context.dataStore.data.first()
            val json = preferences[RECENT_DEVICES_KEY] ?: return emptyList()
            val type = object : TypeToken<List<RemoteDevice>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun saveRecentDevice(device: RemoteDevice) {
        try {
            val currentDevices = getRecentDevices().toMutableList()
            // Remover dispositivo se já existe
            currentDevices.removeAll { it.ip == device.ip && it.port == device.port }
            // Adicionar no início
            currentDevices.add(0, device)
            // Manter apenas os últimos 5
            if (currentDevices.size > 5) {
                currentDevices.removeAt(currentDevices.size - 1)
            }

            val json = gson.toJson(currentDevices)
            context.dataStore.edit { preferences ->
                preferences[RECENT_DEVICES_KEY] = json
            }
        } catch (e: Exception) {
            // Log error
        }
    }

    override suspend fun getUserPreferences(): UserPreferences {
        return try {
            val preferences = context.dataStore.data.first()
            val json = preferences[USER_PREFERENCES_KEY] ?: return UserPreferences()
            gson.fromJson(json, UserPreferences::class.java)
        } catch (e: Exception) {
            UserPreferences()
        }
    }

    override suspend fun saveUserPreferences(preferences: UserPreferences) {
        try {
            val json = gson.toJson(preferences)
            context.dataStore.edit { prefs ->
                prefs[USER_PREFERENCES_KEY] = json
            }
        } catch (e: Exception) {
            // Log error
        }
    }

    override suspend fun getBillingStatus(): BillingStatus {
        return try {
            val preferences = context.dataStore.data.first()
            val json = preferences[BILLING_STATUS_KEY] ?: return BillingStatus()
            gson.fromJson(json, BillingStatus::class.java)
        } catch (e: Exception) {
            BillingStatus()
        }
    }

    override suspend fun saveBillingStatus(status: BillingStatus) {
        try {
            val json = gson.toJson(status)
            context.dataStore.edit { preferences ->
                preferences[BILLING_STATUS_KEY] = json
            }
        } catch (e: Exception) {
            // Log error
        }
    }
}
