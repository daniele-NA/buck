package com.crescenzi.buck.core.compose

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SymbolTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // == LOCAL STATE TO AVOID CURSOR ISSUES WITH DATASTORE ROUND-TRIP == //
    val localValue = remember { mutableStateOf(value) }
    LaunchedEffect(value) { localValue.value = value }

    OutlinedTextField(
        value = localValue.value,
        onValueChange = { input ->
            val filtered = input.filter { !it.isLetterOrDigit() && !it.isWhitespace() }
            val char = filtered.lastOrNull()?.toString() ?: ""
            localValue.value = char
            onValueChange(char)
        },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            color = Color.White
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = AppColors.AccentGreen,
            selectionColors = TextSelectionColors(
                handleColor = AppColors.AccentGreen,
                backgroundColor = AppColors.AccentGreen.copy(alpha = 0.3f)
            ),
            focusedBorderColor = AppColors.AccentGreen,
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
        ),
        modifier = modifier.width(80.dp),
        shape = RoundedCornerShape(12.dp)
    )
}
