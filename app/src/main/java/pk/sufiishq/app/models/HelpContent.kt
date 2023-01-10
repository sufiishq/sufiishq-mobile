package pk.sufiishq.app.models

import androidx.compose.runtime.Composable

data class HelpContent(
    val title: String,
    val content: List<@Composable () -> Unit>
)
