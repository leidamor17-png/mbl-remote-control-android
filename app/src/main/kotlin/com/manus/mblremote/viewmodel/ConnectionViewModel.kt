package com.manus.mblremote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manus.mblremote.model.ConnectionState
import com.manus.mblremote.model.RemoteCommand
import com.manus.mblremote.model.RemoteDevice
import com.manus.mblremote.repository.ConnectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gerenciar estado de conexão com o projetor
 */
class ConnectionViewModel(
    private val repository: ConnectionRepository
) : ViewModel() {

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _recentDevices = MutableStateFlow<List<RemoteDevice>>(emptyList())
    val recentDevices: StateFlow<List<RemoteDevice>> = _recentDevices.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadRecentDevices()
    }

    /**
     * Conectar a um dispositivo remoto
     */
    fun connect(ip: String, port: Int) {
        viewModelScope.launch {
            try {
                _connectionState.value = ConnectionState.Connecting
                _error.value = null

                // Validar IP
                if (!isValidIp(ip)) {
                    _error.value = "IP inválido"
                    _connectionState.value = ConnectionState.Disconnected
                    return@launch
                }

                // Conectar via repository
                val device = repository.connect(ip, port)
                _connectionState.value = ConnectionState.Connected(device)
                saveRecentDevice(device)
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro ao conectar"
                _connectionState.value = ConnectionState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    /**
     * Desconectar do dispositivo
     */
    fun disconnect() {
        viewModelScope.launch {
            try {
                repository.disconnect()
                _connectionState.value = ConnectionState.Disconnected
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /**
     * Enviar comando para o dispositivo remoto
     */
    fun sendCommand(command: RemoteCommand) {
        viewModelScope.launch {
            try {
                repository.sendCommand(command)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /**
     * Carregar dispositivos recentes
     */
    private fun loadRecentDevices() {
        viewModelScope.launch {
            try {
                val devices = repository.getRecentDevices()
                _recentDevices.value = devices
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /**
     * Salvar dispositivo recente
     */
    private fun saveRecentDevice(device: RemoteDevice) {
        viewModelScope.launch {
            try {
                repository.saveRecentDevice(device)
                loadRecentDevices()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /**
     * Validar formato de IP
     */
    private fun isValidIp(ip: String): Boolean {
        val ipPattern = Regex("""^(\d{1,3}\.){3}\d{1,3}$""")
        if (!ipPattern.matches(ip)) return false

        val parts = ip.split(".")
        return parts.all { part ->
            try {
                val num = part.toInt()
                num in 0..255
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Limpar erro
     */
    fun clearError() {
        _error.value = null
    }
}
