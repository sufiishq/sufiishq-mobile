package pk.sufiishq.app.models

import androidx.compose.runtime.MutableState
import androidx.paging.compose.LazyPagingItems
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.helpers.TrackListType

data class KalamItemParam(
    val kalam: Kalam,
    val kalamMenuItems: List<String>,
    val playerDataProvider: PlayerDataProvider,
    val kalamDataProvider: KalamDataProvider,
    val playlistDataProvider: PlaylistDataProvider,
    val lazyKalamItems: LazyPagingItems<Kalam>,
    val playlistItems: List<Playlist>,
    val searchText: MutableState<String>,
    val trackListType: TrackListType
)
