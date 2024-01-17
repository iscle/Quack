import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import component.LoadFileDialog
import component.RSyntaxScrollableTextArea

/*
 * The screen should show the centered app icon (resources/icon_1.jpeg), the app name, recently opened files
 * and an option to open a new file.
 *
 * If there are no recents, it should also be indicated.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FrameWindowScope.WelcomeScreen() {
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

        RSyntaxScrollableTextArea(
            modifier = Modifier.fillMaxSize()
        )
    }

    if (showFilePicker) {
        LoadFileDialog(
            frame = window,
            title = "Open file",
            onFilesSelected = { files ->
                showFilePicker = false
            },
        )
    }
}