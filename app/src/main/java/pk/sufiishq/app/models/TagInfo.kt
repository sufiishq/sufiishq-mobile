package pk.sufiishq.app.models

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

data class TagInfo(
    val first: Int = 0,
    val last: Int = 0,
    val style: String = ""
) {
    fun getStyle(): SpanStyle {
        return when (style) {
            "bold" -> SpanStyle(fontWeight = FontWeight.Bold)
            "italic" -> SpanStyle(fontStyle = FontStyle.Italic)
            else -> SpanStyle()
        }
    }
}
