package pk.sufiishq.app.data.providers

import pk.sufiishq.app.models.Playlist

interface PlaylistDataProvider {

    fun getAll(): List<Playlist>
    fun get(id: Int): Playlist
    fun add(playlist: Playlist)
    fun update(playlist: Playlist)
    fun delete(playlist: Playlist)
}