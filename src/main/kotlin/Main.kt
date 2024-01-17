import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.beust.jcommander.JCommander
import component.ExitDialog
import component.LoadFileDialog
import java.io.File

@Composable
@Preview
fun App(
    args: Array<String>,
    window: ComposeWindow,
) {
    MaterialTheme {
        var openFileDialog by remember { mutableStateOf(false) }
        var selectedFile by remember { mutableStateOf<File?>(null) }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Button(onClick = {
                openFileDialog = true
            }) {
                Text(if (selectedFile == null) "Open file" else "Selected file: ${selectedFile!!.name}")
            }
        }

        if (openFileDialog) {
            LoadFileDialog(
                frame = window,
                title = "Open file",
                onFilesSelected = { files ->
                    selectedFile = files.firstOrNull()
                    openFileDialog = false
                },
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

private fun parseQuackArgs(args: Array<String>): QuackArgs {
    val quackArgs = QuackArgs()

    JCommander.newBuilder()
        .addObject(quackArgs)
        .build()
        .parse(*args)

    return quackArgs
}

fun main(args: Array<String>) {
    val quackArgs = parseQuackArgs(args)

    Settings.init()
    Localization.init(Settings.getSelectedLocale())

    application {
        var showExitDialog by remember { mutableStateOf(false) }
        Window(
            onCloseRequest = {
                showExitDialog = true
            },
            title = Localization.get("app_name"),
        ) {
            setWindowIcon()

//        App(
//            args = args,
//            window = window,
//        )

            WelcomeScreen()

            if (showExitDialog) {
                ExitDialog(
                    onConfirm = ::exitApplication,
                    onDismiss = { showExitDialog = false },
                )
            }
        }
    }
}
