package me.iscle.quack

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun OpenedFileWindow(
    onCloseRequest: () -> Unit,
    file: File,
) {
    val apk = remember(file) { Apk(file) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(apk) {
        isLoading = true
        apk.load()
        isLoading = false
    }

    QuackWindow(
        onCloseRequest = onCloseRequest,
        title = "${file.name} - ${Localization.get("app_name")}",
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                )
            }
        } else {
            Column {
                Row(
                    modifier = Modifier.padding(8.dp)
                ) {
                    var sha256 by remember { mutableStateOf("") }
                    LaunchedEffect(file) {
                        withContext(Dispatchers.IO) {
                            sha256 = file.sha256().toHexString()
                        }
                    }

                    Image(
                        bitmap = apk.getIcon().toComposeImageBitmap(),
                        contentDescription = "App icon",
                        modifier = Modifier.size(64.dp),
                    )

                    Column {
                        BasicTextField(
                            value = apk.getLabel(),
                            onValueChange = {},
                            readOnly = true,
                        )

                        val packageName = apk.manifest?.packageName ?: "Unknown"
                        val versionName = apk.manifest?.versionName ?: "Unknown"
                        val versionCode = apk.manifest?.versionCode ?: "Unknown"

                        BasicTextField(
                            value = "$packageName - $versionName ($versionCode)",
                            onValueChange = {},
                            readOnly = true,
                        )

                        BasicTextField(
                            value = "SHA256: $sha256",
                            onValueChange = {},
                            readOnly = true,
                        )
                    }
                }
            }
        }
    }
}