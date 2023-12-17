package com.ilya.sessions.screen.alertDialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ilya.sessions.R
import com.ilya.theme.LocalColorScheme

@Composable
fun ShowAlertDialog(state: AlertDialogState.Triggered) {
    AlertDialog(
        title = {
            Text(text = stringResource(id = R.string.quit_alert_title))
        },
        confirmButton = {
            Button(onClick = state.onConfirm) {
                Text(text = stringResource(id = R.string.ok))
            }
        },
        dismissButton = {
            Button(onClick = state.onDismiss) {
                Text(text = stringResource(id = R.string.dismiss))
            }
        },
        onDismissRequest = state.onDismiss,
        containerColor = LocalColorScheme.current.cardContainerColor,
        titleContentColor = LocalColorScheme.current.primaryTextColor
    )
}