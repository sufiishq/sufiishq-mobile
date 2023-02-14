package pk.sufiishq.app.models

import pk.sufiishq.aurora.models.DataMenuItem
import pk.sufiishq.aurora.theme.AuroraColor

data class AutoLockDuration(
    val code: Int,
    override val label: String,
    val durationInMillis: Long,
    override val resId: Int? = null,
    override val iconTint: AuroraColor? = null
) : DataMenuItem


