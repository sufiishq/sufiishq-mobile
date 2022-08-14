package pk.sufiishq.app.core.event.events

import pk.sufiishq.app.core.downloader.KalamDownloadState
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.viewmodels.PlayerViewModel

abstract class PlayerEvents : Event(PlayerViewModel::class) {
    object PlayPauseEvent : Event(PlayerViewModel::class)
    object PlayNext : Event(PlayerViewModel::class)
    object PlayPrevious : Event(PlayerViewModel::class)
    object ChangeShuffle : Event(PlayerViewModel::class)
    class UpdateSeekbar(val value: Float) : Event(PlayerViewModel::class)
    class SeekbarChanged(val value: Int) : Event(PlayerViewModel::class)
    object DisposeDownload : Event(PlayerViewModel::class)
    class ChangeDownloadState(val downloadState: KalamDownloadState) : Event(PlayerViewModel::class)
    class StartDownload(val kalam: Kalam) : Event(PlayerViewModel::class)
    class ChangeTrack(val kalam: Kalam, val trackListType: TrackListType) :
        Event(PlayerViewModel::class)
}