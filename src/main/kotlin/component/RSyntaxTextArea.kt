package component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane

@Composable
fun RSyntaxScrollableTextArea(
    modifier: Modifier = Modifier,
) {
    SwingPanel(
        factory = {
            val textArea = RSyntaxTextArea().apply {
                syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_XML
            }

            RTextScrollPane(textArea)
        },
        modifier = modifier,
    )
}