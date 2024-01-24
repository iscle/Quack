package me.iscle.quack.ui.component

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.input.key.*
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import javax.swing.SwingUtilities
import javax.swing.text.DefaultCaret

@Composable
fun RSyntaxScrollableTextArea(
    modifier: Modifier = Modifier,
    text: String,
) {
    SwingPanel(
        factory = {
            val textArea = RSyntaxTextArea().apply {
                syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_XML
                isEditable = false
            }

            RTextScrollPane(textArea)
        },
        modifier = modifier,
        update = {
            if (it.textArea.text.isEmpty()) {
                it.textArea.text = text
                it.textArea.caretPosition = 0
            } else {
                it.textArea.text = text
            }
        }
    )
}