package pk.sufiishq.app.core.help

sealed interface HelpData {
    class Photo(val path: String) : HelpData
    class Divider(val isVisible: Boolean, val height: Int) : HelpData
    class Spacer(val height: Int) : HelpData
    class Paragraph(val text: String) : HelpData
}
