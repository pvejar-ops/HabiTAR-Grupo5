package com.example.habitar.core.ui.componentes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp


@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    // Agregamos este parámetro con un valor por defecto (ej: 12.dp)
    shape: Shape = RoundedCornerShape(12.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape, // <--- Aquí le pasamos el shape al Button de Material3
        colors = colors
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

