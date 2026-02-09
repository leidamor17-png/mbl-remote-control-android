package com.manus.mblremote.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manus.mblremote.model.ConnectionState

@Composable
fun ConnectionStatus(
    state: ConnectionState,
    modifier: Modifier = Modifier
) {
    val (statusColor, statusText, deviceInfo) = when (state) {
        is ConnectionState.Connected -> {
            Triple(
                Color(0xFF10B981),  // Green
                "Conectado",
                state.device.ip
            )
        }
        is ConnectionState.Connecting -> {
            Triple(
                Color(0xFFF59E0B),  // Orange
                "Conectando...",
                null
            )
        }
        is ConnectionState.Error -> {
            Triple(
                Color(0xFFEF4444),  // Red
                "Erro",
                state.message
            )
        }
        is ConnectionState.Disconnected -> {
            Triple(
                Color(0xFFEF4444),  // Red
                "Desconectado",
                null
            )
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Status Indicator
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(statusColor)
        )

        // Text Content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = statusText,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                style = MaterialTheme.typography.labelMedium
            )
            if (deviceInfo != null) {
                Text(
                    text = deviceInfo,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
