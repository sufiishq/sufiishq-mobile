package pk.sufiishq.app.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.helpers.PlayerState
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.Playlist
import java.io.File

// ----------------------------------------- //
// PLAYER DATA PROVIDER
// ----------------------------------------- //
fun dummyPlayerDataProvider() = object : PlayerDataProvider {

    override fun getSeekbarValue(): LiveData<Float> {
        return MutableLiveData(0f)
    }

    override fun updateSeekbarValue(value: Float) {}

    override fun getSeekbarAccess(): LiveData<Boolean> {
        return MutableLiveData(true)
    }

    override fun onSeekbarChanged(value: Float) {}

    override fun getPlayerState(): LiveData<PlayerState> {
        return MutableLiveData(PlayerState.IDLE)
    }

    override fun doPlayOrPause() {}

    override fun getActiveKalam(): LiveData<Kalam?> {
        return MutableLiveData(Kalam(0, "Kalam Title", 1, "1991", "Karachi", "", "", 0, 0))
    }

    override fun changeTrack(kalam: Kalam) {}

    override fun getDownloadProgress(): LiveData<Float> {
        return MutableLiveData(0f)
    }

    override fun getDownloadError(): LiveData<String> {
        return MutableLiveData("")
    }

    override fun setDownloadError(error: String) {}

    override fun startDownload(kalam: Kalam) {}

    override fun disposeDownload() {}
}

// ----------------------------------------- //
// KALAM DATA PROVIDER
// ----------------------------------------- //
fun dummyKalamDataProvider() = object : KalamDataProvider {

    override fun init(trackType: String, playlistId: Int) {}

    override fun getKalamDataFlow(): Flow<PagingData<Kalam>> {
        return emptyFlow()
    }

    override fun searchKalam(keyword: String, trackType: String, playlistId: Int) {}

    override fun update(kalam: Kalam) {}

    override fun delete(kalam: Kalam, trackType: String) {}

    override fun save(sourceKalam: Kalam, splitFile: File, kalamTitle: String) {}

    override fun countAll(): LiveData<Int> = MutableLiveData(0)

    override fun countFavorites(): LiveData<Int> = MutableLiveData(0)

    override fun countDownloads(): LiveData<Int> = MutableLiveData(0)
}

fun dummyTrack() = Kalam(1, "Kalam Title", 2, "1993", "Karachi", "", "", 0, 0)

// ----------------------------------------- //
// PLAYLIST DATA PROVIDER
// ----------------------------------------- //

fun dummyPlaylistDataProvider() = object : PlaylistDataProvider {

    override fun getAll(): LiveData<List<Playlist>> = MutableLiveData(
        listOf(
            Playlist(1, "Karachi"),
            Playlist(2, "Lahore")
        )
    )

    override fun get(id: Int) = MutableLiveData(Playlist(1, "Karachi"))

    override fun add(playlist: Playlist) {}
    override fun update(playlist: Playlist) {}
    override fun delete(playlist: Playlist) {}
    override fun countAll(): LiveData<Int> = MutableLiveData(0)
}

fun dummyPlaylist() = Playlist(1, "Karachi")