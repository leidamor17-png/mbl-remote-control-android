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

    fun connect(ip: String, port: Int) {
        _connectionState.value = ConnectionState.Connecting

        viewModelScope.launch {
            try {
                repository.connect(ip, port)
                _connectionState.value = ConnectionState.Connected
            } catch (e: Exception) {
                _connectionState.value =
                    ConnectionState.Error(e.message ?: "Erro ao conectar")
            }
        }
    }

    fun disconnect() {
        repository.disconnect()
        _connectionState.value = ConnectionState.Disconnected
    }
}
