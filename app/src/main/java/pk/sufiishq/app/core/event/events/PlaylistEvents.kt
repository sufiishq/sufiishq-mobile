package pk.sufiishq.app.core.event.events

import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.viewmodels.PlaylistViewModel

abstract class PlaylistEvents : Event(PlaylistViewModel::class) {
    class ShowPlaylistDialog(val kalam: Kalam?) : PlaylistEvents()
    class AddToPlaylist(val kalam: Kalam, val playlist: Playlist) : PlaylistEvents()
    class ShowAddUpdatePlaylistDialog(val playlist: Playlist?): PlaylistEvents()
    class ShowConfirmDeletePlaylistDialog(val playlist: Playlist?): PlaylistEvents()
    class Add(val playlist: Playlist): PlaylistEvents()
    class Update(val playlist: Playlist): PlaylistEvents()
    class Delete(val playlist: Playlist): PlaylistEvents()
}