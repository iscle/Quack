package me.iscle.quack.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.AwtWindow
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun LoadFileDialog(
    frame: Frame,
    title: String,
    multipleMode: Boolean = false,
    onFilesSelected: (Array<File>) -> Unit,
) {
    AwtWindow(
        create = {
            object : FileDialog(frame, title, LOAD) {
                override fun setVisible(b: Boolean) {
                    super.setVisible(b)
                    if (b) {
                        onFilesSelected(files)
                    }
                }
            }.apply {
                isMultipleMode = multipleMode
            }
        },
        dispose = FileDialog::dispose,
    )
}