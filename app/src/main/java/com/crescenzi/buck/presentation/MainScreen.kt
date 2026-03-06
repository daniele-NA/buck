package com.crescenzi.buck.presentation

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.crescenzi.buck.R
import com.crescenzi.buck.core.compose.AppColors
import com.crescenzi.buck.core.compose.BounceButton
import com.crescenzi.buck.core.compose.ServiceRequiredDialog
import com.crescenzi.buck.core.compose.SymbolTextField

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val triggerKey by viewModel.triggerKey.collectAsState()
    val displayKey = triggerKey.orEmpty()
    val serviceEnabled by viewModel.serviceEnabled.collectAsState()
    val writingEnabled by viewModel.writingEnabled.collectAsState()
    val showDialog = remember { mutableStateOf(false) }

    // == REFRESH SERVICE STATUS ON EVERY RESUME == //
    LifecycleResumeEffect(Unit) {
        viewModel.refreshServiceStatus()
        onPauseOrDispose {}
    }

    if (showDialog.value) {
        ServiceRequiredDialog(
            onOpenSettings = {
                context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            },
            onDismiss = { showDialog.value = false }
        )
    }

    Scaffold(containerColor = AppColors.Bg) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // == HEADER == //
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.AccentGreen
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.subtitle),
                fontSize = 15.sp,
                color = AppColors.TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = AppColors.Divider)
            Spacer(modifier = Modifier.height(24.dp))

            // == SERVICE STATUS == //
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(if (serviceEnabled) AppColors.AccentGreen else AppColors.ErrorRed)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(
                        if (serviceEnabled) R.string.service_enabled else R.string.service_disabled
                    ),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (serviceEnabled) AppColors.AccentGreen else AppColors.ErrorRed
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // == WRITING TOGGLE == //
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        if (writingEnabled) R.string.writing_enabled else R.string.writing_disabled
                    ),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (writingEnabled) AppColors.AccentGreen else AppColors.TextMuted,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = writingEnabled,
                    onCheckedChange = {
                        if (!serviceEnabled && !writingEnabled) {
                            showDialog.value = true
                        } else {
                            viewModel.toggleWriting()
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = AppColors.AccentGreen,
                        uncheckedThumbColor = AppColors.TextMuted,
                        uncheckedTrackColor = AppColors.Divider,
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // == TRIGGER KEY == //
            Text(
                text = stringResource(R.string.trigger_key_label),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.Teal
            )
            Spacer(modifier = Modifier.height(8.dp))
            SymbolTextField(
                value = displayKey,
                onValueChange = { viewModel.saveTriggerKey(it) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // == HINT CARD == //
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.CardBg, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.how_it_works),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.Teal
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.how_it_works_body, displayKey),
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.5f),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // == CTA BUTTON == //
            BounceButton(
                text = stringResource(
                    if (serviceEnabled) R.string.btn_accessibility_settings else R.string.btn_enable_service
                ),
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
