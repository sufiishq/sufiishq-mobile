package pk.sufiishq.aurora.models

import pk.sufiishq.aurora.theme.AuroraColor

interface DataMenuItem {
    val label: String
    val resId: Int?
    val iconTint: AuroraColor?
}
