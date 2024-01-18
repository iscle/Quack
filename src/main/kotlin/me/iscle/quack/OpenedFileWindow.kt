package me.iscle.quack

import androidx.compose.foundation.layout.Column
import androidx.compose.material.TextField
import androidx.compose.runtime.*
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
            var text by remember { mutableStateOf("Text 1") }
            TextField(
                value = text,
                onValueChange = { text = it },
            )

            var text2 by remember { mutableStateOf("Text 2") }
            TextField(
                value = text2,
                onValueChange = { text2 = it },
            )

            var text3 by remember { mutableStateOf("Text 3") }
            TextField(
                value = text3,
                onValueChange = { text3 = it },
            )
        }
    }
}