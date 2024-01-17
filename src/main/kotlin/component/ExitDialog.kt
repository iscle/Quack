package component

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ExitDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onConfirm,
            ) {
                Text(Localization.get("exit"))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
            ) {
                Text(Localization.get("cancel"))
            }
        },
        title = {
            Text(Localization.get("exit"))
        },
        text = {
            Text(Localization.get("exit_confirmation"))
        },
    )
}