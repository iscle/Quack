package me.iscle.quack

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
import androidx.compose.ui.window.application
import com.beust.jcommander.JCommander
import me.iscle.quack.jadx.JadxHelper
import me.iscle.quack.ui.component.ExitDialog
import me.iscle.quack.ui.component.LoadFileDialog
import me.iscle.quack.ui.ApkSummaryWindow
import me.iscle.quack.ui.WelcomeWindow
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

private fun parseQuackArgs(args: Array<String>): QuackArgs {
    val quackArgs = QuackArgs()

    JCommander.newBuilder()
        .addObject(quackArgs)
        .build()
        .parse(*args)

    return quackArgs
}

fun main(args: Array<String>) {
    if (true) {
        val jadxHelper = JadxHelper(File("/Users/iscle/Downloads/app-debug (1).apk"))
        jadxHelper.load()
        jadxHelper.decompile()
        // shared preferences
        val calls = jadxHelper.findMethodCalls("android.content.SharedPreferences.getString")
        println("Calls to SharedPreferences.getString:")
        calls.forEach {
            println("  ${it.methodInfo.fullName}")
            val code = it.parentClass.code
            println("code: $code")
        }

        // shared preferences get boolean
        val calls2 = jadxHelper.findMethodCalls("android.content.SharedPreferences.getBoolean")
        println("Calls to SharedPreferences.getBoolean:")
        calls2.forEach {
            println("  ${it.methodInfo.fullName}")
        }

        // shared preferences get int
        val calls3 = jadxHelper.findMethodCalls("android.content.SharedPreferences.getInt")
        println("Calls to SharedPreferences.getInt:")
        calls3.forEach {
            println("  ${it.methodInfo.fullName}")
        }
        return
    }

    val quackArgs = parseQuackArgs(args)

    Settings.init(quackArgs)
    Localization.init(Settings.selectedLocale)

    application {
        var closeRequested by remember { mutableStateOf(false) }
        var openedFiles = remember { mutableStateListOf<File>(
            File("/home/iscle/Downloads/Adif_2.0.4_Apkpure.apk"),
        ) }

        var showWelcomeWindow by remember { mutableStateOf(true) }
        if (showWelcomeWindow) {
            WelcomeWindow(
                onCloseRequest = {
                    showWelcomeWindow = false
                    if (!showWelcomeWindow && openedFiles.isEmpty()) exitApplication()
                },
                onOpenFiles = { files ->
                    openedFiles.addAll(files)
                }
            )
        }

        for (file in openedFiles) {
            key(file) {
                ApkSummaryWindow(
                    onCloseRequest = {
                        openedFiles.remove(file)
                        if (!showWelcomeWindow && openedFiles.isEmpty()) exitApplication()
                    },
                    file = file,
                )
            }
        }

        if (closeRequested) {
            ExitDialog(
                onConfirm = { dontShowAgain ->
                    if (dontShowAgain) {
                        Settings.showExitDialog = false
                    }
                    exitApplication()
                },
                onDismiss = { closeRequested = false },
            )
        }
    }
}
