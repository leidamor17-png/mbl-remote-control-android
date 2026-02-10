package com.manus.mblremote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manus.mblremote.model.ConnectionState
import com.manus.mblremote.repository.ConnectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConnectionViewModel(
    private val repository: ConnectionRepository
) : ViewModel() {

    private val _connectionState =
        MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun connect(ip: String, port: Int) {
        viewModelScope.launch {
            try {
                // 🔹 SIMULA conexão (por enquanto)
                if (ip.isBlank() || port <= 0) {
                    throw IllegalArgumentException("IP ou porta inválidos")
                }

                // 👉 AQUI no futuro entra o socket real
                // repository.connect(ip, port)

                // ✅ Simulação de sucesso
                _connectionState.value = ConnectionState.Connected

            } catch (e: Exception) {
                _connectionState.value = ConnectionState.Error
                _errorMessage.value =
                    "Não foi possível conectar. Verifique o IP e a porta."
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            // repository.disconnect()
            _connectionState.value = ConnectionState.Disconnected
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
