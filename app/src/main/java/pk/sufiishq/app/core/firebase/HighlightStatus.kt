package pk.sufiishq.app.core.firebase

import androidx.annotation.DrawableRes
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.getString
import pk.sufiishq.aurora.theme.AuroraColor

sealed class HighlightStatus(
    val bgColor: AuroraColor,
    @DrawableRes val leadingIcon: Int,
    val label: String,
) {
    class OnGoing(
        bgColor: AuroraColor = AuroraColor.Green,
        leadingIcon: Int = R.drawable.round_error_outline_24,
        label: String = getString(R.string.msg_highlight_ongoing)
    ) : HighlightStatus(bgColor, leadingIcon, label)

    class Expired(
        bgColor: AuroraColor = AuroraColor.OnError,
        leadingIcon: Int = R.drawable.round_error_outline_24,
        label: String = getString(R.string.msg_highlight_expired)
    ) : HighlightStatus(bgColor, leadingIcon, label)
}