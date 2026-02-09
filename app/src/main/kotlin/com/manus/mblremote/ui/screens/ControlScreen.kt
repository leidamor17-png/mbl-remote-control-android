package com.manus.mblremote.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manus.mblremote.model.CommandType
import com.manus.mblremote.model.ConnectionState
import com.manus.mblremote.model.RemoteCommand
import com.manus.mblremote.ui.components.ConnectionStatus
import com.manus.mblremote.ui.components.DirectionalKey
import com.manus.mblremote.ui.components.DirectionalPad
import com.manus.mblremote.ui.components.VolumeControl

@Composable
fun ControlScreen(
    connectionState: ConnectionState,
    onDirectionalPress: (DirectionalKey) -> Unit,
    onVolumeUp: () -> Unit,
    onVolumeDown: () -> Unit,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onDisconnect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isConnected = connectionState is ConnectionState.Connected

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (connectionState is ConnectionState.Connected)
                        connectionState.device.name
                    else
                        "Controle Remoto",
                    fontSize = 28.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Connection Status
            ConnectionStatus(state = connectionState, modifier = Modifier.fillMaxWidth())

            // Navigation Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Navegação",
                    fontSize = 14.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                DirectionalPad(
                    onPress = { key ->
                        val commandType = when (key) {
                            DirectionalKey.UP -> CommandType.UP
                            DirectionalKey.DOWN -> CommandType.DOWN
                            DirectionalKey.LEFT -> CommandType.LEFT
                            DirectionalKey.RIGHT -> CommandType.RIGHT
                            DirectionalKey.OK -> CommandType.OK
                        }
                        // Aqui seria chamado o envio do comando
                        onDirectionalPress(key)
                    },
                    enabled = isConnected
                )
            }

            // Volume Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                VolumeControl(
                    onVolumeUp = onVolumeUp,
                    onVolumeDown = onVolumeDown,
                    enabled = isConnected
                )
            }

            // Action Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onBack,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        enabled = isConnected,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Voltar",
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    Button(
                        onClick = onHome,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        enabled = isConnected,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Home",
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }

        // Disconnect Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onDisconnect,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Desconectar",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
