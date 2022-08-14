package pk.sufiishq.app.core.player

import pk.sufiishq.app.core.player.listener.PlayerStateListener
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam

interface AudioPlayer {

    fun getActiveTrack(): Kalam
    fun setSource(source: Kalam, trackListType: TrackListType)
    fun doPlayOrPause()
    fun isPlaying(): Boolean
    fun seekTo(msec: Int)
    fun release()
    fun getTrackListType(): TrackListType
    fun registerListener(listener: PlayerStateListener): Boolean
}