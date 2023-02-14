package pk.sufiishq.app.models

import pk.sufiishq.aurora.models.DataMenuItem
import pk.sufiishq.aurora.theme.AuroraColor

data class SecurityQuestion(val index: Int, val question: String) :
    DataMenuItem {

    var answer: String = ""

    override val label: String = question
    override val resId: Int? get() = null
    override val iconTint: AuroraColor? = null
}