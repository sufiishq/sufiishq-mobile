package pk.sufiishq.app.models

import pk.sufiishq.app.helpers.PlayerState

data class KalamInfo(
    val playerState: PlayerState,
    val kalam: Kalam,
    var currentProgress: Int,
    val totalDuration: Int,
    val enableSeekbar: Boolean
)
