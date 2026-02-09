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

@Composable
fun VolumeControl(
    onVolumeUp: () -> Unit,
    onVolumeDown: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val buttonSize = 56.dp
    val cornerRadius = 12.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(buttonSize)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Volume Down
        VolumeButton(
            text = "−",
            size = buttonSize,
            cornerRadius = cornerRadius,
            onClick = onVolumeDown,
            enabled = enabled
        )

        // Volume Label
        Text(
            text = "Volume",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.width(60.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Volume Up
        VolumeButton(
            text = "+",
            size = buttonSize,
            cornerRadius = cornerRadius,
            onClick = onVolumeUp,
            enabled = enabled
        )
    }
}

@Composable
private fun VolumeButton(
    text: String,
    size: androidx.compose.ui.unit.Dp,
    cornerRadius: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit,
    enabled: Boolean = true
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
            fontSize = 18.sp,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
