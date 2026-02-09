package com.manus.mblremote.network

import com.google.gson.Gson
import com.manus.mblremote.model.RemoteCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.Socket

/**
 * Cliente Socket para comunicação com o App Companion via rede local
 */
class SocketClient {

    private var socket: Socket? = null
    private var outputStream: PrintWriter? = null
    private var inputStream: BufferedReader? = null
    private val gson = Gson()

    /**
     * Conectar ao servidor (App Companion)
     */
    suspend fun connect(ip: String, port: Int) = withContext(Dispatchers.IO) {
        try {
            socket = Socket(ip, port)
            outputStream = PrintWriter(socket!!.getOutputStream(), true)
            inputStream = BufferedReader(InputStreamReader(socket!!.getInputStream()))
        } catch (e: Exception) {
            throw Exception("Falha ao conectar: ${e.message}")
        }
    }

    /**
     * Desconectar do servidor
     */
    suspend fun disconnect() = withContext(Dispatchers.IO) {
        try {
            outputStream?.close()
            inputStream?.close()
            socket?.close()
            socket = null
            outputStream = null
            inputStream = null
        } catch (e: Exception) {
            throw Exception("Falha ao desconectar: ${e.message}")
        }
    }

    /**
     * Enviar comando para o servidor
     */
    suspend fun sendCommand(command: RemoteCommand) = withContext(Dispatchers.IO) {
        try {
            if (socket == null || !socket!!.isConnected) {
                throw Exception("Socket não conectado")
            }

            val json = gson.toJson(command)
            outputStream?.println(json)
        } catch (e: Exception) {
            throw Exception("Falha ao enviar comando: ${e.message}")
        }
    }

    /**
     * Verificar se está conectado
     */
    fun isConnected(): Boolean {
        return socket != null && socket!!.isConnected
    }

    /**
     * Receber resposta do servidor (para implementação futura)
     */
    suspend fun receiveResponse(): String? = withContext(Dispatchers.IO) {
        try {
            if (socket == null || !socket!!.isConnected) {
                return@withContext null
            }
            return@withContext inputStream?.readLine()
        } catch (e: Exception) {
            throw Exception("Falha ao receber resposta: ${e.message}")
        }
    }
}
