package pk.sufiishq.app.models

import pk.sufiishq.app.core.help.HelpData

data class HelpContent(
    val title: String,
    val content: List<HelpData>
)
