package me.iscle.quack.ui.component

import me.iscle.quack.Localization
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow

@Composable
fun ExitDialog(
    onConfirm: (dontShowAgain: Boolean) -> Unit,
    onDismiss: () -> Unit,
) {
    DialogWindow(
        onCloseRequest = onDismiss,
        state = DialogState(size = DpSize.Unspecified),
        title = Localization.get("exit"),
        undecorated = true,
    ) {
        WindowDraggableArea {
            var dontShowAgain by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // TODO: Icon
                    Text(
                        text = Localization.get("exit"),
                        modifier = Modifier.alpha(ContentAlpha.high),
                        style = MaterialTheme.typography.subtitle1,
                    )
                }

                Text(
                    text = Localization.get("exit_confirmation"),
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    style = MaterialTheme.typography.body2,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = dontShowAgain,
                        onCheckedChange = { dontShowAgain = it },
                    )
                    Text(Localization.get("dont_show_again"))
                }

                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                    ) {
                        Text(Localization.get("cancel"))
                    }

                    Button(
                        onClick = {
                            onConfirm(dontShowAgain)
                        },
                    ) {
                        Text(Localization.get("exit"))
                    }
                }
            }
        }
    }
}