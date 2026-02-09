package com.manus.mblremote.repository

import com.manus.mblremote.model.RemoteCommand
import com.manus.mblremote.model.RemoteDevice
import com.manus.mblremote.network.SocketClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Repository para gerenciar conexões e comunicação com dispositivos remotos
 */
class ConnectionRepository(
    private val socketClient: SocketClient,
    private val dataStore: PreferencesDataStore
) {

    /**
     * Conectar a um dispositivo remoto
     */
    suspend fun connect(ip: String, port: Int): RemoteDevice = withContext(Dispatchers.IO) {
        // Conectar via socket
        socketClient.connect(ip, port)

        // Criar objeto de dispositivo
        val device = RemoteDevice(
            id = UUID.randomUUID().toString(),
            name = "Projetor MBL",
            ip = ip,
            port = port,
            isConnected = true
        )

        return@withContext device
    }

    /**
     * Desconectar do dispositivo
     */
    suspend fun disconnect() = withContext(Dispatchers.IO) {
        socketClient.disconnect()
    }

    /**
     * Enviar comando para o dispositivo
     */
    suspend fun sendCommand(command: RemoteCommand) = withContext(Dispatchers.IO) {
        socketClient.sendCommand(command)
    }

    /**
     * Obter dispositivos recentes
     */
    suspend fun getRecentDevices(): List<RemoteDevice> = withContext(Dispatchers.IO) {
        return@withContext dataStore.getRecentDevices()
    }

    /**
     * Salvar dispositivo recente
     */
    suspend fun saveRecentDevice(device: RemoteDevice) = withContext(Dispatchers.IO) {
        dataStore.saveRecentDevice(device)
    }

    /**
     * Obter preferências do usuário
     */
    suspend fun getUserPreferences() = withContext(Dispatchers.IO) {
        return@withContext dataStore.getUserPreferences()
    }

    /**
     * Salvar preferências do usuário
     */
    suspend fun saveUserPreferences(preferences: com.manus.mblremote.model.UserPreferences) =
        withContext(Dispatchers.IO) {
            dataStore.saveUserPreferences(preferences)
        }

    /**
     * Obter status de billing
     */
    suspend fun getBillingStatus() = withContext(Dispatchers.IO) {
        return@withContext dataStore.getBillingStatus()
    }

    /**
     * Salvar status de billing
     */
    suspend fun saveBillingStatus(status: com.manus.mblremote.model.BillingStatus) =
        withContext(Dispatchers.IO) {
            dataStore.saveBillingStatus(status)
        }
}

/**
 * Interface para armazenamento de preferências
 */
interface PreferencesDataStore {
    suspend fun getRecentDevices(): List<RemoteDevice>
    suspend fun saveRecentDevice(device: RemoteDevice)
    suspend fun getUserPreferences(): com.manus.mblremote.model.UserPreferences
    suspend fun saveUserPreferences(preferences: com.manus.mblremote.model.UserPreferences)
    suspend fun getBillingStatus(): com.manus.mblremote.model.BillingStatus
    suspend fun saveBillingStatus(status: com.manus.mblremote.model.BillingStatus)
}
