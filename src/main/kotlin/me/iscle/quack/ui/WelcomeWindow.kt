package me.iscle.quack.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.iscle.quack.ui.component.LoadFileDialog
import me.iscle.quack.ui.component.RSyntaxScrollableTextArea
import java.io.File

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WelcomeWindow(
    onCloseRequest: () -> Unit,
    onOpenFiles: (List<File>) -> Unit,
) {
    QuackWindow(
        onCloseRequest = onCloseRequest,
    ) {
        var showFilePicker by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // show the centered app icon (resources/icon_1.jpeg)
            Image(
                painter = painterResource("icon_1.jpeg"),
                contentDescription = "App icon",
                modifier = Modifier.size(128.dp),
            )
            // show the app name
            Text(
                text = "Quack",
            )
            // show recently opened files
            // show an option to open a new file
            Button(
                onClick = {
                    showFilePicker = true
                },
            ) {
                Text("Open file")
            }

            // language spinner for English (en_US) and Catalan (ca_ES)
            var isLanguageDropdownExpanded by remember { mutableStateOf(false) }
            var selectedText by remember { mutableStateOf("English") }
            ExposedDropdownMenuBox(
                expanded = isLanguageDropdownExpanded,
                onExpandedChange = { isLanguageDropdownExpanded = it },
            ) {
                TextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isLanguageDropdownExpanded) },
//                modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = isLanguageDropdownExpanded,
                    onDismissRequest = {
                        isLanguageDropdownExpanded = false
                    }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            selectedText = "English"
                            isLanguageDropdownExpanded = false
                        }
                    ) {
                        Text(text = "English")
                    }

                    DropdownMenuItem(
                        onClick = {
                            selectedText = "Català"
                            isLanguageDropdownExpanded = false
                        }
                    ) {
                        Text(text = "Català")
                    }
                }
            }
        }

        if (showFilePicker) {
            LoadFileDialog(
                frame = window,
                title = "Open file",
                multipleMode = true,
                onFilesSelected = { files ->
                    showFilePicker = false
                    if (files.isNotEmpty()) {
                        onOpenFiles(files.asList())
                        onCloseRequest()
                    }
                },
            )
        }
    }
}