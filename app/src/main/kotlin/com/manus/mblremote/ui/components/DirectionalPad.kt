package com.manus.mblremote.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class DirectionalKey {
    UP, DOWN, LEFT, RIGHT, OK
}

@Composable
fun DirectionalPad(
    onPress: (DirectionalKey) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val buttonSize = 56.dp
    val centerSize = 72.dp
    val cornerRadius = 12.dp

    Column(
        modifier = modifier
            .width(centerSize + 32.dp)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // UP Button
        DirectionalButton(
            text = "UP",
            size = buttonSize,
            cornerRadius = cornerRadius,
            onClick = { onPress(DirectionalKey.UP) },
            enabled = enabled
        )

        // Middle Row: LEFT, OK, RIGHT
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LEFT Button
            DirectionalButton(
                text = "LEFT",
                size = buttonSize,
                cornerRadius = cornerRadius,
                onClick = { onPress(DirectionalKey.LEFT) },
                enabled = enabled
            )

            // OK Button (Center)
            DirectionalButton(
                text = "OK",
                size = centerSize,
                cornerRadius = cornerRadius,
                onClick = { onPress(DirectionalKey.OK) },
                enabled = enabled,
                isCenter = true
            )

            // RIGHT Button
            DirectionalButton(
                text = "RIGHT",
                size = buttonSize,
                cornerRadius = cornerRadius,
                onClick = { onPress(DirectionalKey.RIGHT) },
                enabled = enabled
            )
        }

        // DOWN Button
        DirectionalButton(
            text = "DOWN",
            size = buttonSize,
            cornerRadius = cornerRadius,
            onClick = { onPress(DirectionalKey.DOWN) },
            enabled = enabled
        )
    }
}

@Composable
private fun DirectionalButton(
    text: String,
    size: androidx.compose.ui.unit.Dp,
    cornerRadius: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit,
    enabled: Boolean = true,
    isCenter: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(cornerRadius)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .graphicsLayer {
                alpha = if (enabled) 1f else 0.5f
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = if (isCenter) 14.sp else 12.sp,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
