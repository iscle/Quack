package me.iscle.quack.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.iscle.quack.Apk
import me.iscle.quack.Localization
import me.iscle.quack.ui.component.RSyntaxScrollableTextArea
import me.iscle.quack.sha256
import java.io.File

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun ApkSummaryWindow(
    onCloseRequest: () -> Unit,
    file: File,
) {
    QuackWindow(
        onCloseRequest = onCloseRequest,
        title = "${file.name} - ${Localization.get("app_name")}",
    ) {
        val apk = remember(file) { Apk(file) }
        var isLoading by remember { mutableStateOf(true) }
        val codeViewWindowStates = remember { mutableStateListOf<CodeViewWindowState>() }

        LaunchedEffect(apk) {
            isLoading = true
            apk.load()
            isLoading = false
        }

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
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
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

                Divider(
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Button(
                        onClick = {
                            codeViewWindowStates.add(
                                CodeViewWindowState(
                                    title = "AndroidManifest.xml - ${file.name} - ${Localization.get("app_name")}",
                                    code = apk.stringManifest ?: "Error loading manifest",
                                )
                            )
                        },
                    ) {
                        Text("AndroidManifest.xml")
                    }
                }

                RSyntaxScrollableTextArea(
                    modifier = Modifier.fillMaxSize(),
                    text = apk.stringManifest ?: "",
                )
            }
        }

        codeViewWindowStates.forEach { state ->
            key(state) {
                CodeViewWindow(
                    state = state,
                    onCloseRequest = { codeViewWindowStates.remove(state) },
                )
            }
        }
    }
}