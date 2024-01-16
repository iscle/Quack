import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.AwtWindow
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.FileDialog

@Composable
@Preview
fun App(
    window: ComposeWindow,
) {
    MaterialTheme {
        var openFileDialog by remember { mutableStateOf(false) }
        var selectedFile by remember { mutableStateOf<String?>(null) }

        Button(onClick = {
            openFileDialog = true
        }) {
            Text(if (selectedFile == null) "Open file" else "Selected file: $selectedFile")
        }

        if (openFileDialog) {
            AwtWindow(
                create = {
                    object : FileDialog(window, "Select file", LOAD) {
                        override fun setVisible(visible: Boolean) {
                            super.setVisible(visible)
                            if (visible) {
                                openFileDialog = false
                                selectedFile = file
                            }
                        }
                    }
                },
                dispose = FileDialog::dispose,
            )
        }
    }
}

@Composable
private fun FrameWindowScope.setWindowIcon() {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    val options = arrayOf(
        "icon_1.jpeg",
        "icon_2.jpeg",
        "icon_3.jpeg",
        "icon_4.jpeg",
        "duck.png",
    )
    val icon = painterResource(options.random())

    SideEffect {
        window.iconImage = icon.toAwtImage(density, layoutDirection)
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Quack",
    ) {
        setWindowIcon()
        App(
            window = window,
        )
    }
}
