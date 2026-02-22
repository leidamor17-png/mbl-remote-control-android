package com.manus.mblremote.model

import java.io.Serializable

/**
 * Tipos de comandos que podem ser enviados para o Companion
 */
enum class CommandType {
    UP, DOWN, LEFT, RIGHT, OK,
    BACK, HOME,
    VOLUME_UP, VOLUME_DOWN,
    MOUSE_MOVE, MOUSE_CLICK,
    TEXT_INPUT,
    SHORTCUT_YOUTUBE, SHORTCUT_NETFLIX, SHORTCUT_HDMI1, SHORTCUT_HDMI2
}

/**
 * Modelo de comando a ser enviado via rede
 */
data class RemoteCommand(
    val type: CommandType,
    val timestamp: Long = System.currentTimeMillis(),
    val payload: String? = null
) : Serializable

/**
 * Modelo de dispositivo conectado
 */
data class RemoteDevice(
    val id: String,
    val name: String,
    val ip: String,
    val port: Int,
    val lastConnected: Long = System.currentTimeMillis(),
    val isConnected: Boolean = false
) : Serializable

/**
 * Estado de conexão
 */
sealed class ConnectionState {
    object Disconnected : ConnectionState()
    object Connecting : ConnectionState()
    object Connected : ConnectionState()   // ✅ CORRIGIDO AQUI
    data class Error(val message: String) : ConnectionState()
}

/**
 * Preferências do usuário
 */
data class UserPreferences(
    val onboardingCompleted: Boolean = false,
    val hapticFeedbackEnabled: Boolean = true,
    val autoConnectEnabled: Boolean = false,
    val selectedTheme: String = "dark",
    val isPremium: Boolean = false,
    val lastConnectedDeviceId: String? = null
) : Serializable

/**
 * Status de billing/compra
 */
data class BillingStatus(
    val isPremium: Boolean = false,
    val purchaseToken: String? = null,
    val purchaseTime: Long? = null,
    val isAutoRenewing: Boolean = false
) : Serializable

/**
 * Cores do tema
 */
data class ThemeColors(
    val backgroundPrimary: Long,
    val backgroundSecondary: Long,
    val accentPrimary: Long,
    val accentSecondary: Long,
    val textPrimary: Long,
    val textSecondary: Long,
    val success: Long,
    val error: Long,
    val warning: Long
)
