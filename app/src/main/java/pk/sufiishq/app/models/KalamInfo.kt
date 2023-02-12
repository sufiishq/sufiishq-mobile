package pk.sufiishq.app.models

import pk.sufiishq.app.core.player.PlayerState
import pk.sufiishq.app.helpers.TrackListType

data class KalamInfo(
    val playerState: PlayerState,
    val kalam: Kalam,
    var currentProgress: Int,
    val totalDuration: Int,
    val enableSeekbar: Boolean,
    val trackListType: TrackListType
)
