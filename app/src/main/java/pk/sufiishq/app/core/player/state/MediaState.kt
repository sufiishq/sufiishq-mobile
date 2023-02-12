package pk.sufiishq.app.core.player.state

import pk.sufiishq.app.core.player.PlayerState
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.app.utils.quickToast

sealed class MediaState(val kalam: Kalam, val trackListType: TrackListType) {

    class Idle(kalam: Kalam, trackListType: TrackListType) : MediaState(kalam, trackListType)
    class Loading(kalam: Kalam, trackListType: TrackListType) : MediaState(kalam, trackListType)
    class Playing(
        kalam: Kalam,
        val currentProgress: Int,
        val totalDuration: Int,
        trackListType: TrackListType
    ) :
        MediaState(kalam, trackListType)

    class Pause(
        kalam: Kalam,
        val currentProgress: Int,
        val totalDuration: Int,
        trackListType: TrackListType
    ) : MediaState(kalam, trackListType)

    class Resume(
        kalam: Kalam,
        val currentProgress: Int,
        val totalDuration: Int,
        trackListType: TrackListType
    ) : MediaState(kalam, trackListType)

    class Complete(kalam: Kalam, trackListType: TrackListType) : MediaState(kalam, trackListType)
    class Stop(kalam: Kalam, trackListType: TrackListType) : MediaState(kalam, trackListType)
    class Error(
        kalam: Kalam,
        val what: Int,
        val extra: Int,
        val message: String,
        trackListType: TrackListType
    ) :
        MediaState(kalam, trackListType)
}

fun MediaState.mapToIdleState(): KalamInfo {

    if (this is MediaState.Error) {
        quickToast(this.message)
    }

    return KalamInfo(
        PlayerState.IDLE,
        this.kalam,
        0,
        0,
        false,
        this.trackListType
    )
}

fun MediaState.mapToLoadingState(kalamInfo: KalamInfo): KalamInfo {
    return kalamInfo.copy(
        playerState = PlayerState.LOADING,
        kalam = this.kalam,
        enableSeekbar = false,
        trackListType = this.trackListType
    )
}

fun MediaState.Playing.mapToPlayingState(
    kalamInfo: KalamInfo?,
    seekbarEnableOnPlaying: Boolean
): KalamInfo {
    return if (seekbarEnableOnPlaying) {
        KalamInfo(
            PlayerState.PLAYING,
            this.kalam,
            this.currentProgress,
            this.totalDuration,
            true,
            this.trackListType
        )
    } else {
        kalamInfo!!
    }
}

fun MediaState.Pause.mapToPauseState(): KalamInfo {
    return KalamInfo(
        PlayerState.PAUSE,
        this.kalam,
        this.currentProgress,
        this.totalDuration,
        false,
        this.trackListType
    )
}

fun MediaState.Resume.mapToResumeState(): KalamInfo {
    return KalamInfo(
        PlayerState.PLAYING,
        this.kalam,
        this.currentProgress,
        this.totalDuration,
        true,
        this.trackListType
    )
}