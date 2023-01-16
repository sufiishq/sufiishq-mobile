package pk.sufiishq.app.core.help

import androidx.compose.ui.text.AnnotatedString

sealed interface HelpData {
    class Photo(val path: String) : HelpData
    class Divider(val height: Int) : HelpData
    class Spacer(val height: Int) : HelpData
    class Paragraph(val text: AnnotatedString) : HelpData
}
