package me.iscle.quack.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import me.iscle.quack.ui.component.RSyntaxScrollableTextArea

data class CodeViewWindowState(
    val title: String,
    val code: String,
)

@Composable
fun CodeViewWindow(
    state: CodeViewWindowState,
    onCloseRequest: () -> Unit,
) {
    var showSearchBar by remember { mutableStateOf(false) }

    QuackWindow(
        onCloseRequest = onCloseRequest,
        title = state.title,
        onKeyEvent = {
            println(it)
            when {
                it.type == KeyEventType.KeyDown && it.isCtrlPressed && it.key == Key.W -> {
                    onCloseRequest()
                    true
                }
                it.type == KeyEventType.KeyDown && it.isCtrlPressed && it.key == Key.F -> {
                    showSearchBar = true
                    true
                }
                else -> false
            }
        }
    ) {
        Column {
            if (showSearchBar) {
                Text("Search bar goes here")
            }
            Divider()
            RSyntaxScrollableTextArea(
                modifier = Modifier.fillMaxSize(),
                text = state.code,
            )
        }
    }
}