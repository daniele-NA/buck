package com.crescenzi.buck.core.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.crescenzi.buck.R

@Composable
fun ServiceRequiredDialog(
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(AppColors.DialogBg)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // == ICON == //
            Icon(
                imageVector = Icons.Rounded.Warning,
                contentDescription = null,
                tint = AppColors.ErrorRed,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.dialog_service_required_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.dialog_service_required_body),
                fontSize = 14.sp,
                color = AppColors.TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // == BUTTONS == //
            Button(
                onClick = {
                    onOpenSettings()
                    onDismiss()
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.AccentGreen
                )
            ) {
                Text(
                    text = stringResource(R.string.dialog_open_settings),
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AppColors.TextMuted
                )
            ) {
                Text(
                    text = stringResource(R.string.dialog_dismiss),
                    fontSize = 14.sp
                )
            }
        }
    }
}
