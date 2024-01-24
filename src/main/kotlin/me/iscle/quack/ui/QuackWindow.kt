package me.iscle.quack.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState

@Composable
fun QuackWindow(
    onCloseRequest: () -> Unit,
    state: WindowState = rememberWindowState(),
    visible: Boolean = true,
    title: String = "Untitled",
    icon: Painter? = painterResource("icon_1.jpeg"),
    undecorated: Boolean = false,
    transparent: Boolean = false,
    resizable: Boolean = true,
    enabled: Boolean = true,
    focusable: Boolean = true,
    alwaysOnTop: Boolean = false,
    onPreviewKeyEvent: (KeyEvent) -> Boolean = { false },
    onKeyEvent: (KeyEvent) -> Boolean = { false },
    content: @Composable FrameWindowScope.() -> Unit
) = Window(
    onCloseRequest,
    state,
    visible,
    title,
    null,
    undecorated,
    transparent,
    resizable,
    enabled,
    focusable,
    alwaysOnTop,
    onPreviewKeyEvent,
    onKeyEvent,
) {
    if (icon != null) setWindowIcon(icon)
    MaterialTheme {
        Surface {
            content()
        }
    }
}

@Composable
private fun FrameWindowScope.setWindowIcon(icon: Painter) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    SideEffect {
        window.iconImage = icon.toAwtImage(density, layoutDirection)
    }
}
