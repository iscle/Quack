package me.iscle.quack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun OpenedFileWindow(
    onCloseRequest: () -> Unit,
    file: File,
) {
    QuackWindow(
        onCloseRequest = onCloseRequest,
        title = "${file.name} - ${Localization.get("app_name")}",
    ) {
        Column {
            Row {
                var sha256 by remember { mutableStateOf("") }
                LaunchedEffect(file) {
                    withContext(Dispatchers.IO) {
                        sha256 = Sha256.ofFile(file)
                    }
                }

                BasicTextField(
                    value = "File name: ${file.name}\n" +
                            "SHA256: $sha256",
                    onValueChange = {},
                    readOnly = true,
                )
            }
        }
    }
}